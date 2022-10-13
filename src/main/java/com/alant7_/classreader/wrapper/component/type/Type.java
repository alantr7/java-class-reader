package com.alant7_.classreader.wrapper.component.type;

public interface Type {

    String getClassPath();

    String getFullName();

    String getSimpleName();

    Class<?> loadClass() throws Exception;

    Class<?> loadClass(ClassLoader classLoader) throws Exception;

    boolean isEnum();

    boolean isArray();

    boolean isAnnotation();

    boolean isPrimitive();

    static Type of(String type) {
        return TypeImpl.of(type);
    }

}
