package amidst.mojangapi.minecraftinterface.legacy;

import amidst.clazz.translator.ClassTranslator;

public enum InfdevClassTranslator {
    INSTANCE;

    private final ClassTranslator classTranslator = createClassTranslator();

    public static ClassTranslator get() { return INSTANCE.classTranslator; }

    private static String getFieldChunkBlocks() {
        switch (InfdevMinecraftInterface.SELECTED_VERSION) {
            case inf_20100327:
                return "e";
            case inf_20100330_1:
            case inf_20100330_2:
            case inf_20100413:
            case inf_20100414:
            case inf_20100415:
            case inf_20100420:
            case inf_20100607:
            case inf_20100608:
                return "f";
            case inf_20100611:
            case inf_20100615:
                return "g";
            case inf_20100616:
            case inf_20100617_1:
            case inf_20100617_2:
            case inf_20100618:
            case inf_20100624:
            case inf_20100625_1:
            case inf_20100625_2:
            case inf_20100627:
            case inf_20100629:
            case inf_20100630_1:
            case inf_20100630_2:
                return "h";
        }
        return "g";
    }

    // @formatter:off
    private ClassTranslator createClassTranslator() {
        return ClassTranslator
            .builder()
                .ifDetect(c ->
                    c.searchForStringContaining("RandomSeed")
                    && c.searchForStringContaining("level.dat")
                    && c.searchForStringContaining("SizeOnDisk"))
                .thenDeclareRequired(InfdevSymbolicNames.CLASS_WORLD)
                    .requiredField(InfdevSymbolicNames.FIELD_WORLD_SEED, "l")
            .next()
                .ifDetect(c ->
                    c.searchForStringContaining("xPos")
                    && c.searchForStringContaining("zPos")
                    && c.searchForStringContaining("HeightMap"))
                .thenDeclareRequired(InfdevSymbolicNames.CLASS_CHUNK)
                   .requiredField(InfdevSymbolicNames.FIELD_CHUNK_BLOCKS, getFieldChunkBlocks())
                   .requiredMethod(InfdevSymbolicNames.METHOD_CHUNK_CALLBACK, "a").end()
            .next()
                .ifDetect(c -> c.searchForLong(341873128712L) && c.searchForLong(341873128712L))
                .thenDeclareRequired(InfdevSymbolicNames.CLASS_CHUNK_GENERATOR)
                    .requiredConstructor(InfdevSymbolicNames.CONSTRUCTOR_CHUNK_GENERATOR).symbolic(InfdevSymbolicNames.CLASS_WORLD).real("long").end()
                    .requiredMethod(InfdevSymbolicNames.METHOD_CHUNK_GENERATOR_GENERATE, "b").real("int").real("int").end()
            .construct();
    }
    // @formatter:on
}
