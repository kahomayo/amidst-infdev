package amidst.mojangapi.minecraftinterface.legacy;

import amidst.clazz.symbolic.declaration.SymbolicClassDeclaration;
import amidst.clazz.symbolic.declaration.SymbolicMethodDeclaration;
import amidst.mojangapi.minecraftinterface.RecognisedVersion;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public enum InfdevClassLoaders {
    ;

    public static URLClassLoader from(URLClassLoader existing, RecognisedVersion version, Map<SymbolicClassDeclaration, String> classMap) {
        Map<String, Set<String>> symbolicMethodsByClass = Collections.singletonMap(
                InfdevSymbolicNames.CLASS_CHUNK,
                Collections.singleton(InfdevSymbolicNames.METHOD_CHUNK_CALLBACK)
        );

        Map<String, String> symbolicToRealClassNames = classMap.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getSymbolicClassName(), Map.Entry::getValue));
        Map<String, Set<StubMethod>> stubMethods = classMap.entrySet().stream()
                .filter(entry -> symbolicMethodsByClass.containsKey(entry.getKey().getSymbolicClassName()))
                .collect(Collectors.toMap(Map.Entry::getValue, entry -> toStubMethods(symbolicMethodsByClass, entry.getKey(), symbolicToRealClassNames)));
        return new StubbingClassLoader(existing.getURLs(), stubMethods);
    }

    private static Set<StubMethod> toStubMethods(Map<String, Set<String>> symbolicMethodsByClass, SymbolicClassDeclaration declaration, Map<String, String> symbolicToRealClassNames) {
        return toStubMethods(
            symbolicMethodsByClass.get(declaration.getSymbolicClassName()),
            declaration.getMethods(),
            symbolicToRealClassNames
        );
    }

    private static Set<StubMethod> toStubMethods(Set<String> symbolicNamesToStub, List<SymbolicMethodDeclaration> methodDeclarations, Map<String, String> symbolicToRealClassNames) {
        return methodDeclarations.stream().filter(declaration -> symbolicNamesToStub.contains(declaration.getSymbolicName()))
                .map(declaration -> toStubMethod(declaration, symbolicToRealClassNames))
                .collect(Collectors.toSet());
    }

    private static StubMethod toStubMethod(SymbolicMethodDeclaration methodDeclaration, Map<String, String> symbolicToRealClassNames) {
        // This method does not yet support signatures other than void(), but could be expanded to arbitrary parameters.
        return StubMethod.of(methodDeclaration.getRealName(), new Type[0]);
    }

    private static final class StubMethod {
        private final String name;
        private final String targetDescriptor;

        public static StubMethod of(String name, Type[] parameterTypes) {
            return new StubMethod(name, Type.getMethodDescriptor(Type.VOID_TYPE, parameterTypes));
        }

        private StubMethod(String name, String targetDescriptor) {
            this.name = name;
            this.targetDescriptor = targetDescriptor;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StubMethod that = (StubMethod) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(targetDescriptor, that.targetDescriptor);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, targetDescriptor);
        }
    }

    public static class StubbingClassLoader extends URLClassLoader {
        private final Set<String> packages;
        private final Map<String, Set<StubMethod>> stubMethodsForClass;

        public StubbingClassLoader(URL[] urls, Map<String, Set<StubMethod>> stubMethodsForClass) {
            super(urls);
            this.packages = stubMethodsForClass.keySet().stream()
                    .map(StubbingClassLoader::getPackageName)
                    .collect(Collectors.toSet());
            this.stubMethodsForClass = stubMethodsForClass;
        }

        private static String getPackageName(String className) {
            int lastDot = className.lastIndexOf('.');
            return className.substring(0, Integer.max(lastDot, 0));
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            // if any class in a package needs to be handled by us, then all
            // of them do. Otherwise, signatures wouldn't match up and the JVM
            // would refuse to load the class.
            if (packages.contains(getPackageName(name))) {
                byte[] bytes = getClassBytes(name);
                return defineClass(name, bytes, 0, bytes.length);
            } else {
                return super.findClass(name);
            }
        }


        private byte[] getClassBytes(String className) throws ClassNotFoundException {
            InputStream classResource = getResourceAsStream(className.replace(".", "/") + ".class");
            if (classResource == null) {
                throw new ClassNotFoundException("Failed to find .class file for manipulating");
            }
            ClassReader cr;
            try {
                cr = new ClassReader(classResource);
            } catch (IOException e) {
                throw new ClassNotFoundException("Failed to construct class reader", e);
            }
            ClassWriter cw = new ClassWriter(cr, 0);
            Set<StubMethod> methodsToStub = stubMethodsForClass.getOrDefault(className, Collections.emptySet());
            cr.accept(new MethodStubbingVisitor(cw, methodsToStub), 0);
            return cw.toByteArray();
        }
    }

    private static class MethodStubbingVisitor extends ClassVisitor {
        private final Set<StubMethod> methodsToStub;

        public MethodStubbingVisitor(ClassWriter classWriter, Set<StubMethod> methodsToStub) {
            super(Opcodes.ASM8, classWriter);
            this.methodsToStub = methodsToStub;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (methodsToStub.contains(new StubMethod(name, descriptor))) {
                return new BodyDeletingVisitor(mv, (Type.getArgumentsAndReturnSizes(descriptor) >> 2));
            } else {
                return mv;
            }
        }
    }

    private static class BodyDeletingVisitor extends MethodVisitor {
        private final MethodVisitor writer;
        private final int maxLocals;

        public BodyDeletingVisitor(MethodVisitor writer, int maxLocals) {
            super(Opcodes.ASM8);
            this.writer = writer;
            this.maxLocals = maxLocals;
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            writer.visitMaxs(0, this.maxLocals);
        }

        @Override
        public void visitCode() {
            writer.visitCode();
            writer.visitInsn(Opcodes.RETURN);
        }

        @Override
        public void visitEnd() {
            writer.visitEnd();
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            return writer.visitAnnotation(descriptor, visible);
        }

        @Override
        public void visitParameter(String name, int access) {
            writer.visitParameter(name, access);
        }
    }


}
