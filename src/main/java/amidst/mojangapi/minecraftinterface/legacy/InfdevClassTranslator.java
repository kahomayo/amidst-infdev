package amidst.mojangapi.minecraftinterface.legacy;

import amidst.clazz.translator.ClassTranslator;
import amidst.mojangapi.minecraftinterface.RecognisedVersion;

public enum InfdevClassTranslator {
    INSTANCE;

    public static ClassTranslator get(RecognisedVersion v) { return INSTANCE.createClassTranslator(v); }

    private static String getFieldChunkBlocks(RecognisedVersion v) {
        if (RecognisedVersion.isOlderOrEqualTo(v, RecognisedVersion._infdev_20100327))
            return "e";
        else if (RecognisedVersion.isOlder(v, RecognisedVersion._infdev_20100611))
            return "f";
        else if (RecognisedVersion.isOlder(v, RecognisedVersion._infdev_20100617_1))
            return "g";
        else if (RecognisedVersion.isOlder(v, RecognisedVersion._infdev_20100624))
            return "h";
        else
            return "b";
    }

    private static String getFieldWorldSeed(RecognisedVersion v) {
        if (RecognisedVersion.isOlder(v, RecognisedVersion._infdev_20100618))
            return "l";
        else if (RecognisedVersion.isOlder(v, RecognisedVersion._infdev_20100630))
            return "m";
        else
            return "n";
    }

    private String getChunkCallback(RecognisedVersion v) {
        if (RecognisedVersion.isOlderOrEqualTo(RecognisedVersion._infdev_20100630, v))
            return "b";
        else
            return "a";
    }

    // @formatter:off
    private ClassTranslator createClassTranslator(RecognisedVersion v) {
        return ClassTranslator
            .builder()
                .ifDetect(c ->
                    c.searchForStringContaining("RandomSeed")
                    && c.searchForStringContaining("level.dat")
                    && c.searchForStringContaining("SizeOnDisk"))
                .thenDeclareRequired(InfdevSymbolicNames.CLASS_WORLD)
                    .requiredField(InfdevSymbolicNames.FIELD_WORLD_SEED, getFieldWorldSeed(v))
            .next()
                .ifDetect(c -> {
                    // TODO: test if this can be gotten rid of
                    if (RecognisedVersion.isOlder(v, RecognisedVersion._infdev_20100624))
                        return c.searchForStringContaining("xPos")
                                && c.searchForStringContaining("zPos")
                                && c.searchForStringContaining("HeightMap");
                    else
                        return c.searchForStringContaining("Attempted to place a tile entity where there was no entity tile!");
                })
                .thenDeclareRequired(InfdevSymbolicNames.CLASS_CHUNK)
                   .requiredField(InfdevSymbolicNames.FIELD_CHUNK_BLOCKS, getFieldChunkBlocks(v))
                   .requiredMethod(InfdevSymbolicNames.METHOD_CHUNK_CALLBACK, getChunkCallback(v)).end()
            .next()
                .ifDetect(c -> c.searchForLong(341873128712L) && c.searchForLong(341873128712L))
                .thenDeclareRequired(InfdevSymbolicNames.CLASS_CHUNK_GENERATOR)
                    .requiredConstructor(InfdevSymbolicNames.CONSTRUCTOR_CHUNK_GENERATOR).symbolic(InfdevSymbolicNames.CLASS_WORLD).real("long").end()
                    .requiredMethod(InfdevSymbolicNames.METHOD_CHUNK_GENERATOR_GENERATE, "b").real("int").real("int").end()
            .construct();
    }
    // @formatter:on
}
