package amidst.mojangapi.minecraftinterface.legacy;

import amidst.clazz.real.AccessFlags;
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
                return "h";
            case inf_20100624:
            case inf_20100625_1:
            case inf_20100625_2:
            case inf_20100627:
            case inf_20100629:
            case inf_20100630_1:
            case inf_20100630_2:
                return "b";
        }
        return "g";
    }

    private static String getFieldWorldSeed() {
        switch (InfdevMinecraftInterface.SELECTED_VERSION) {
            case inf_20100327:
            case inf_20100330_1:
            case inf_20100330_2:
            case inf_20100413:
            case inf_20100414:
            case inf_20100415:
            case inf_20100420:
            case inf_20100607:
            case inf_20100608:
            case inf_20100611:
            case inf_20100615:
            case inf_20100616:
            case inf_20100617_1:
            case inf_20100617_2:
            case inf_20100618:
                return "l";
            case inf_20100624:
            case inf_20100625_1:
            case inf_20100625_2:
            case inf_20100627:
            case inf_20100629:
            case inf_20100630_1:
            case inf_20100630_2:
                return "m";
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
                    .requiredField(InfdevSymbolicNames.FIELD_WORLD_SEED, getFieldWorldSeed())
            .next()
                .ifDetect(c -> {
                            if (InfdevMinecraftInterface.SELECTED_VERSION.ordinal() < InfdevMinecraftInterface.InfdevVersion.inf_20100624.ordinal())
                                return c.searchForStringContaining("xPos")
                                        && c.searchForStringContaining("zPos")
                                        && c.searchForStringContaining("HeightMap");
                            else
                                return c.searchForStringContaining("Attempted to place a tile entity where there was no entity tile!");
                })
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
