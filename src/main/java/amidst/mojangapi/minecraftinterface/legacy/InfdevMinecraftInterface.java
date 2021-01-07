package amidst.mojangapi.minecraftinterface.legacy;

import amidst.clazz.symbolic.SymbolicClass;
import amidst.clazz.symbolic.SymbolicObject;
import amidst.mojangapi.minecraftinterface.MinecraftInterface;
import amidst.mojangapi.minecraftinterface.MinecraftInterfaceException;
import amidst.mojangapi.minecraftinterface.RecognisedVersion;
import amidst.mojangapi.minecraftinterface.UnsupportedDimensionException;
import amidst.mojangapi.world.Dimension;
import amidst.mojangapi.world.WorldOptions;
import amidst.mojangapi.world.biome.Biome;
import amidst.mojangapi.world.versionfeatures.DefaultBiomes;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;

public class InfdevMinecraftInterface implements MinecraftInterface {
    public static final RecognisedVersion LAST_COMPATIBLE_VERSION = RecognisedVersion._infdev;
    private final RecognisedVersion recognisedVersion;
    private final SymbolicClass chunkGeneratorClass;
    private final SymbolicClass chunkClass;
    private final SymbolicClass worldClass;

    public InfdevMinecraftInterface(RecognisedVersion recognisedVersion, SymbolicClass chunkGeneratorClass, SymbolicClass chunkClass, SymbolicClass worldClass) {
        this.recognisedVersion = recognisedVersion;
        this.chunkGeneratorClass = chunkGeneratorClass;
        this.chunkClass = chunkClass;
        this.worldClass = worldClass;
    }

    public InfdevMinecraftInterface(Map<String, SymbolicClass> symbolicClassMap, RecognisedVersion recognisedVersion) {
        this(recognisedVersion,
            symbolicClassMap.get(InfdevSymbolicNames.CHUNK_GENERATOR_CLASS),
            symbolicClassMap.get(InfdevSymbolicNames.CLASS_CHUNK),
            symbolicClassMap.get(InfdevSymbolicNames.CLASS_WORLD));
    }

    @Override
    public synchronized  WorldAccessor createWorldAccessor(WorldOptions worldOptions) throws MinecraftInterfaceException {
        try {
            // And here we come upon a sad state of affairs...
            // At the very end of METHOD_CHUNK_GENERATOR_GENERATE, `chunk.a()` is called, which invokes a method
            // on the chunk's world instance. Thus we have to construct a world. Constructing a world always attempts
            // to create those directories. The best way I found to deal with this was to use a temp directory.
            // I don't think the generated chunks actually get written to disk, but that's not really much help.
            Path worldDir = Files.createTempDirectory("amidst-infdev-tmpworld");
            worldDir.toFile().deleteOnExit();
            SymbolicObject world = worldClass.callConstructor(InfdevSymbolicNames.CTOR_WORLD, worldDir.toFile(), "world1");
            SymbolicObject chunkGenerator = chunkGeneratorClass.callConstructor(InfdevSymbolicNames.CHUNK_GENERATOR_CTOR, world.getObject(), worldOptions.getWorldSeed().getLong());
            return new WorldAccessor(chunkGenerator);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IOException e) {
            throw new MinecraftInterfaceException("unable to create chunk generator", e);
        }
    }

    @Override
    public RecognisedVersion getRecognisedVersion() {
        return recognisedVersion;
    }

    private class WorldAccessor implements MinecraftInterface.WorldAccessor {
        private final SymbolicObject chunkGenerator;

        public WorldAccessor(SymbolicObject chunkGenerator) {
            this.chunkGenerator = chunkGenerator;
        }

        @Override
        public synchronized <T> T getBiomeData(Dimension dimension, int x, int y, int width, int height, boolean useQuarterResolution, Function<int[], T> biomeDataMapper) throws MinecraftInterfaceException {
            if (dimension != Dimension.OVERWORLD)
                throw new UnsupportedDimensionException(dimension);
            int shift = useQuarterResolution ? 4 : 1;
            int blockXBegin = x * shift;
            int blockXEnd = (x + width) * shift;
            int blockYBegin = y * shift;
            int blockYEnd = (y + height) * shift;


            int chunkXBegin = (int) Math.floor(blockXBegin / 16.0);
            int chunkXEnd = (int) Math.floor(blockXEnd / 16.0);
            int chunkZBegin = (int) Math.floor(blockYBegin / 16.0);
            int chunkZEnd = (int) Math.floor(blockYEnd / 16.0);

            int[][][] biomesByChunk = new int[chunkZEnd - chunkZBegin + 1][][];
            for (int chunkZ = chunkZBegin; chunkZ <= chunkZEnd; ++chunkZ) {
                biomesByChunk[chunkZ - chunkZBegin] = new int[chunkXEnd - chunkXBegin + 1][];
                for (int chunkX = chunkXBegin; chunkX <= chunkXEnd; ++chunkX) {
                    try {
                        // call the generator to produce the chunk
                        Object chunk = chunkGenerator.callMethod(InfdevSymbolicNames.METHOD_CHUNK_GENERATOR_GENERATE, chunkX, chunkZ);
                        // get the chunk's data array
                        byte[] blocks = (byte[]) new SymbolicObject(chunkClass, chunk).getFieldValue(InfdevSymbolicNames.FIELD_CHUNK_BLOCKS);

                        // convert to fake biomes
                        int[] biomes = IntStream.range(0, 16)
                                .flatMap(blockZ -> IntStream.range(0, 16)
                                        .map(blockX -> getIndex(blockZ, 63, blockX)))
                                .map(i -> blocks[i] == 9 || blocks[i] == 8 ? DefaultBiomes.ocean : DefaultBiomes.plains)
                                .toArray();

                        // for (int yy = 0; yy < 16; ++yy) {
                        //     for (int xx = 0; xx < 16; ++xx) {
                        //         System.out.print(biomes[xx + yy * 16]);
                        //     }
                        //     System.out.print('\n');
                        // }

                        // add to result array
                        biomesByChunk[chunkZ - chunkZBegin][chunkX - chunkXBegin] = biomes;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new MinecraftInterfaceException("Failed to generate chunk", e);
                    }
                }
            }
            int[] result = new int[width * height];
            for (int idxY = 0; idxY < height; ++idxY)  {
                for (int idxX = 0; idxX < width; ++idxX) {
                    int blockX = (x + idxX) * shift;
                    int chunkX = (int) Math.floor(blockX / 16.0);
                    int blockY = (y + idxY) * shift;
                    int chunkY = (int) Math.floor(blockY / 16.0);
                    result[idxX + idxY * width] = biomesByChunk[chunkY - chunkZBegin][chunkX - chunkXBegin][
                            (blockX - chunkX * 16) + (blockY - chunkY * 16) * 16];
                }
            }
            // invoke mapper with result array
            return biomeDataMapper.apply(result);
        }

        private int getIndex(int blockX, int blockY, int blockZ) {
            return blockY + blockX * 128 + blockZ * 16 * 128;
        }

        @Override
        public Set<Dimension> supportedDimensions() {
            return Set.of(Dimension.OVERWORLD);
        }
    }
}
