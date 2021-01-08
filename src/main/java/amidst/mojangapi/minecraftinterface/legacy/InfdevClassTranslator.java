package amidst.mojangapi.minecraftinterface.legacy;

import amidst.clazz.translator.ClassTranslator;

public enum InfdevClassTranslator {
    INSTANCE;

    private final ClassTranslator classTranslator = createClassTranslator();

    public static ClassTranslator get() { return INSTANCE.classTranslator; }

    // @formatter:off
    private ClassTranslator createClassTranslator() {
        return ClassTranslator
            .builder()
                .ifDetect(c ->
                    c.searchForStringContaining("RandomSeed")
                    && c.searchForStringContaining("level.dat")
                    && c.searchForStringContaining("SizeOnDisk"))
                .thenDeclareRequired(InfdevSymbolicNames.CLASS_WORLD)
            .next()
                .ifDetect(c ->
                    c.searchForStringContaining("xPos")
                    && c.searchForStringContaining("zPos")
                    && c.searchForStringContaining("HeightMap"))
                .thenDeclareRequired(InfdevSymbolicNames.CLASS_CHUNK)
                   .requiredField(InfdevSymbolicNames.FIELD_CHUNK_BLOCKS, "g")
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
