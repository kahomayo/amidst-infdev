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
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

public class InfdevMinecraftInterface implements MinecraftInterface {
    public static final RecognisedVersion LAST_COMPATIBLE_VERSION = RecognisedVersion._infdev_20100630;
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
        if (RecognisedVersion.isOlder(RecognisedVersion._infdev_20100615, recognisedVersion)) {
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
                        int[] biomes = BiomeMapper.byHeight(blocks);

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

        @Override
        public Set<Dimension> supportedDimensions() {
            return Collections.singleton(Dimension.OVERWORLD);
        }
    }

    private enum BiomeMapper {
        ;

        public static int[] byWater(byte[] blocks) {
            return streamY63()
                    .map(i -> blocks[i] == 9 || blocks[i] == 8 ? DefaultBiomes.ocean : DefaultBiomes.plains)
                    .toArray();
        }

        private static IntStream streamY63() {
            return IntStream.range(0, 16)
                    .flatMap(blockZ -> IntStream.range(0, 16)
                            .map(blockX -> getIndex(blockZ, 63, blockX)));
        }

        public static int[] byY63(byte[] blocks) {
            return streamY63()
                    .map(i -> {
                        switch (blocks[i]) {
                            case 8:
                            case 9:
                                return DefaultBiomes.ocean;
                            case 12:
                            case 13:
                                return DefaultBiomes.beach;
                            case 2:
                            case 3:
                                return DefaultBiomes.plains;
                            default:
                                return DefaultBiomes.extremeHills;
                        }
                    }).toArray();
        }

        public static int[] byHeight(byte[] blocks) {
            int[] result = new int[256];
            for (int z = 0; z < 16; ++z) {
                for (int x = 0; x < 16; ++x) {
                    int finalX = x;
                    int finalZ = z;
                    int maxY = IntStream.iterate(127, y -> y - 1).limit(128)
                            .filter(y -> {
                                int i = getIndex(finalX, y, finalZ);
                                return blocks[i] != 0 && blocks[i] != 9 && blocks[i] != 8;
                            })
                            .findFirst().orElse(0);
                    int biome;
                    if (maxY < 55) {
                        biome = DefaultBiomes.deepOcean;
                    } else if (maxY < 63){
                        biome = DefaultBiomes.ocean;
                    } else if (maxY < 68) {
                        biome = DefaultBiomes.beach;
                    } else if (maxY < 80) {
                        biome = DefaultBiomes.plains;
                    } else if (maxY < 90) {
                        biome = DefaultBiomes.extremeHillsEdge;
                    } else if (maxY < 100) {
                        biome = DefaultBiomes.extremeHills;
                    } else if (maxY < 127) {
                        biome = DefaultBiomes.extremeHillsM;
                    } else {
                        biome = DefaultBiomes.extremeHillsPlus;
                    }
                    result[z + x * 16] = biome;
                }
            }
            return result;
        }



        private static int getIndex(int blockX, int blockY, int blockZ) {
            return blockY + blockX * 128 + blockZ * 16 * 128;
        }

    }
}
