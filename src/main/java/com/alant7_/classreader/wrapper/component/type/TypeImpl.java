package com.alant7_.classreader.wrapper.component.type;

import com.alant7_.classreader.wrapper.component.ClassInfo;
import lombok.Getter;

public class TypeImpl implements Type {

    public static final TypeImpl VOID = new TypeImpl("java.lang.Void");

    @Getter
    private final String classPath;

    @Getter
    private final String fullName;

    @Getter
    private final boolean isArray;

    protected TypeImpl(String classPath) {
        this(classPath, classPath);
    }

    protected TypeImpl(String classPath, String fullName) {
        this(classPath, fullName, false);
    }

    protected TypeImpl(String classPath, String fullName, boolean isArray) {
        this.classPath = classPath;
        this.fullName = fullName;
        this.isArray = isArray;
    }

    @Override
    public String getSimpleName() {
        return classPath.contains(".") ? classPath.substring(classPath.lastIndexOf('.') + 1) : classPath;
    }

    public Class<?> loadClass() throws Exception {
        return Class.forName(classPath);
    }

    public Class<?> loadClass(ClassLoader classLoader) throws Exception {
        return Class.forName(classPath, true, classLoader);
    }

    @Override
    public boolean isEnum() {
        return false;
    }

    @Override
    public boolean isAnnotation() {
        return false;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    // TODO: Load class info from its file
    public ClassInfo getInfo() {
        return null;
    }

    public static TypeImpl of(String type) {
        return createType(type);
    }

    private static TypeImpl createType(String type) {
        int nestedArrays = 0;

        for (int charIndex = 0; charIndex < type.length(); charIndex++) {
            char t = type.charAt(charIndex);
            var t1 = switch (t) {
                case 'D' -> PrimitiveType.DOUBLE;
                case 'F' -> PrimitiveType.FLOAT;
                case 'I' -> PrimitiveType.INTEGER;
                case 'J' -> PrimitiveType.LONG;

                case 'B' -> PrimitiveType.BYTE;
                case 'C' -> PrimitiveType.CHAR;
                case 'S' -> PrimitiveType.SHORT;
                case 'Z' -> PrimitiveType.BOOLEAN;

                case 'V' -> TypeImpl.VOID;

                case 'L' -> {
                    String path = type.substring(nestedArrays + 1).replace('/', '.');
                    if (path.endsWith(";")) {
                        path = path.substring(0, path.length() - 1);
                    }
                    yield new TypeImpl(path, path);
                }

                case '[' -> {
                    nestedArrays++;
                    yield null;
                }

                default -> {
                    System.out.println("Could not parse: " + type);
                    yield null;
                }
            };

            if (t1 != null)
                return new TypeImpl(t1.classPath, t1.classPath + "[]".repeat(nestedArrays), true);

            if (nestedArrays != 0)
                continue;

            return null;
        }

        return null;
    }

}
