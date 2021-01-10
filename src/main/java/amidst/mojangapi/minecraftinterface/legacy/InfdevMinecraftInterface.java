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
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

public class InfdevMinecraftInterface implements MinecraftInterface {
    public static final RecognisedVersion LAST_COMPATIBLE_VERSION = RecognisedVersion._infdev_0630;
    public static final InfdevVersion SELECTED_VERSION = InfdevVersion.inf_20100618;
    private final RecognisedVersion recognisedVersion;
    private final SymbolicClass chunkGeneratorClass;
    private final SymbolicClass chunkClass;
    private final SymbolicClass worldClass;
    private final Objenesis objenesis = new ObjenesisStd();
    private final ObjectInstantiator<Object> worldInstantiator;


    public InfdevMinecraftInterface(RecognisedVersion recognisedVersion, SymbolicClass chunkGeneratorClass, SymbolicClass chunkClass, SymbolicClass worldClass) {
        this.recognisedVersion = recognisedVersion;
        this.chunkGeneratorClass = chunkGeneratorClass;
        this.chunkClass = chunkClass;
        this.worldClass = worldClass;
        this.worldInstantiator = objenesis.getInstantiatorOf((Class<Object>) worldClass.getClazz());
    }

    public InfdevMinecraftInterface(Map<String, SymbolicClass> symbolicClassMap, RecognisedVersion recognisedVersion) {
        this(recognisedVersion,
            symbolicClassMap.get(InfdevSymbolicNames.CLASS_CHUNK_GENERATOR),
            symbolicClassMap.get(InfdevSymbolicNames.CLASS_CHUNK),
            symbolicClassMap.get(InfdevSymbolicNames.CLASS_WORLD));
    }

    @Override
    public WorldAccessor createWorldAccessor(WorldOptions worldOptions) throws MinecraftInterfaceException {
        try {
            long seed = worldOptions.getWorldSeed().getLong();
            SymbolicObject chunkGenerator = chunkGeneratorClass.callConstructor(
                    InfdevSymbolicNames.CONSTRUCTOR_CHUNK_GENERATOR, makeWorld(seed), seed);
            return new WorldAccessor(chunkGenerator);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new MinecraftInterfaceException("unable to create chunk generator", e);
        }
    }

    private Object makeWorld(long seed) throws IllegalAccessException {
        if (InfdevVersion.inf_20100615.ordinal() <= SELECTED_VERSION.ordinal()) {
            Object instance = worldInstantiator.newInstance();
            worldClass.getField(InfdevSymbolicNames.FIELD_WORLD_SEED).getRawField().set(instance, seed);
            return instance;
        } else {
            return null;
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
                        SymbolicObject symChunk = (chunk instanceof SymbolicObject) ? (SymbolicObject) chunk : new SymbolicObject(chunkClass, chunk);
                        // get the chunk's data array
                        byte[] blocks = (byte[]) symChunk.getFieldValue(InfdevSymbolicNames.FIELD_CHUNK_BLOCKS);

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

    public enum InfdevVersion {
        inf_20100327,
        inf_20100330_1,
        inf_20100330_2,
        // inf_201004xx,
        inf_20100413,
        inf_20100414,
        inf_20100415,
        inf_20100420,
        inf_20100607,
        inf_20100608,
        inf_20100611,
        inf_20100615,
        inf_20100616,
        inf_20100617_1,
        inf_20100617_2,
        inf_20100618,
        inf_20100624,
        inf_20100625_1,
        inf_20100625_2,
        inf_20100627,
        inf_20100629,
        inf_20100630_1,
        inf_20100630_2,
    }
}
