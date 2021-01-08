package amidst.mojangapi.minecraftinterface.legacy;

import amidst.clazz.symbolic.SymbolicClass;
import amidst.clazz.symbolic.SymbolicObject;
import amidst.mojangapi.minecraftinterface.MinecraftInterface;
import amidst.mojangapi.minecraftinterface.MinecraftInterfaceException;
import amidst.mojangapi.minecraftinterface.RecognisedVersion;
import amidst.mojangapi.minecraftinterface.UnsupportedDimensionException;
import amidst.mojangapi.world.Dimension;
import amidst.mojangapi.world.WorldOptions;
import amidst.mojangapi.world.versionfeatures.DefaultBiomes;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

public class InfdevMinecraftInterface implements MinecraftInterface {
    public static final RecognisedVersion LAST_COMPATIBLE_VERSION = RecognisedVersion._infdev;
    private final RecognisedVersion recognisedVersion;
    private final SymbolicClass chunkGeneratorClass;
    private final SymbolicClass chunkClass;

    public InfdevMinecraftInterface(RecognisedVersion recognisedVersion, SymbolicClass chunkGeneratorClass, SymbolicClass chunkClass) {
        this.recognisedVersion = recognisedVersion;
        this.chunkGeneratorClass = chunkGeneratorClass;
        this.chunkClass = chunkClass;
    }

    public InfdevMinecraftInterface(Map<String, SymbolicClass> symbolicClassMap, RecognisedVersion recognisedVersion) {
        this(recognisedVersion,
            symbolicClassMap.get(InfdevSymbolicNames.CLASS_CHUNK_GENERATOR),
            symbolicClassMap.get(InfdevSymbolicNames.CLASS_CHUNK));
    }

    @Override
    public WorldAccessor createWorldAccessor(WorldOptions worldOptions) throws MinecraftInterfaceException {
        try {
            SymbolicObject chunkGenerator = chunkGeneratorClass.callConstructor(InfdevSymbolicNames.CONSTRUCTOR_CHUNK_GENERATOR, null, worldOptions.getWorldSeed().getLong());
            return new WorldAccessor(chunkGenerator);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
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
        public <T> T getBiomeData(Dimension dimension, int x, int y, int width, int height, boolean useQuarterResolution, Function<int[], T> biomeDataMapper) throws MinecraftInterfaceException {
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
            return Collections.singleton(Dimension.OVERWORLD);
        }
    }
}
