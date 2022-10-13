package com.alant7_.classreader.wrapper.component.type;

import lombok.Getter;

public class PrimitiveType<T> extends TypeImpl {

    public static final PrimitiveType<Double> DOUBLE = new PrimitiveType<>(double.class, "java.lang.Double");
    public static final PrimitiveType<Float> FLOAT = new PrimitiveType<>(float.class, "java.lang.Float");
    public static final PrimitiveType<Integer> INTEGER = new PrimitiveType<>(int.class, "java.lang.Integer");
    public static final PrimitiveType<Long> LONG = new PrimitiveType<>(long.class, "java.lang.Long");


    public static final PrimitiveType<Byte> BYTE = new PrimitiveType<>(byte.class, "java.lang.Byte");
    public static final PrimitiveType<Character> CHAR = new PrimitiveType<>(char.class, "java.lang.Character");
    public static final PrimitiveType<Short> SHORT = new PrimitiveType<>(short.class, "java.lang.Short");
    public static final PrimitiveType<Boolean> BOOLEAN = new PrimitiveType<>(boolean.class, "java.lang.Boolean");

    @Getter
    private final Class<T> handle;

    protected PrimitiveType(Class<T> handle, String classPath) {
        super(classPath);
        this.handle = handle;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }
}
