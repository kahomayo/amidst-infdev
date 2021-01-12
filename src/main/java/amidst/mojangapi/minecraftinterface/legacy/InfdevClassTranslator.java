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
        else if (RecognisedVersion.isOlder(v, RecognisedVersion._a1_0_2_02))
            return "n";
        else if (RecognisedVersion.isOlder(v, RecognisedVersion._a1_0_4))
            return "p";
        else if (RecognisedVersion.isOlder(v, RecognisedVersion._a1_0_16))
            return "q";
        else if (RecognisedVersion.isOlder(v, RecognisedVersion._a1_0_17_04))
            return "r";
        else if (RecognisedVersion.isOlder(v, RecognisedVersion._a1_1_0))
            return "t";
        else
            return "u";
    }

    private String getChunkCallback(RecognisedVersion v) {
        if (RecognisedVersion.isOlder(v, RecognisedVersion._infdev_20100630))
            return "a";
        else if (RecognisedVersion.isOlder(v, RecognisedVersion._a1_0_10))
            return "b";
        else
            return "c";
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
                .ifDetect(c -> c.searchForStringContaining("Attempted to place a tile entity where there was no entity tile!"))
                .thenDeclareRequired(InfdevSymbolicNames.CLASS_CHUNK)
                   .requiredField(InfdevSymbolicNames.FIELD_CHUNK_BLOCKS, getFieldChunkBlocks(v))
                   .requiredMethod(InfdevSymbolicNames.METHOD_CHUNK_CALLBACK, getChunkCallback(v)).end()
            .next()
                .ifDetect(c -> c.searchForLong(341873128712L))
                .thenDeclareRequired(InfdevSymbolicNames.CLASS_CHUNK_GENERATOR)
                    .requiredConstructor(InfdevSymbolicNames.CONSTRUCTOR_CHUNK_GENERATOR).symbolic(InfdevSymbolicNames.CLASS_WORLD).real("long").end()
                    .requiredMethod(InfdevSymbolicNames.METHOD_CHUNK_GENERATOR_GENERATE, "b").real("int").real("int").end()
            .construct();
    }
    // @formatter:on
}
