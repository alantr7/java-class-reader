package com.alant7_.classreader.io;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class ConstantPoolTag<T> {

    public static final ConstantPoolTag<String> UTF8 = new ConstantPoolTag<>(1, 2);
    public static final ConstantPoolTag<Integer> INTEGER = new ConstantPoolTag<>(3, 4);
    public static final ConstantPoolTag<Float> FLOAT = new ConstantPoolTag<>(4, 4);
    public static final ConstantPoolTag<Long> LONG = new ConstantPoolTag<>(5, 8);
    public static final ConstantPoolTag<Double> DOUBLE = new ConstantPoolTag<>(6, 8);

    public static final ConstantPoolTag<Integer> CLASS_REFERENCE = new ConstantPoolTag<>(7, 2);
    public static final ConstantPoolTag<Integer> STRING_REFERENCE = new ConstantPoolTag<>(8, 2);
    public static final ConstantPoolTag<Integer[]> FIELD_REFERENCE = new ConstantPoolTag<>(9, 4);
    public static final ConstantPoolTag<Integer[]> METHOD_REFERENCE = new ConstantPoolTag<>(10, 4);
    public static final ConstantPoolTag<Integer[]> INTERFACE_METHOD_REFERENCE = new ConstantPoolTag<>(11, 4);

    public static final ConstantPoolTag<Integer[]> NAME_TYPE_DESCRIPTOR = new ConstantPoolTag<>(12, 4);
    public static final ConstantPoolTag<Integer[]> METHOD_HANDLE = new ConstantPoolTag<>(15, 3);
    public static final ConstantPoolTag<Integer> METHOD_TYPE = new ConstantPoolTag<>(16, 2);
    public static final ConstantPoolTag<?> DYNAMIC = new ConstantPoolTag<>(17, 4);
    public static final ConstantPoolTag<?> INVOKE_DYNAMIC = new ConstantPoolTag<>(18, 4);
    public static final ConstantPoolTag<?> MODULE = new ConstantPoolTag<>(19, 2);
    public static final ConstantPoolTag<?> PACKAGE = new ConstantPoolTag<>(20, 2);

    private static final ConstantPoolTag<?>[] values = new ConstantPoolTag[17];

    static {
        int results = 0;
        for (var field : ConstantPoolTag.class.getDeclaredFields()) {
            if (field.getType() == ConstantPoolTag.class) {
                try {
                    var tag = (ConstantPoolTag<?>) field.get(null);
                    tag.name = field.getName();
                    values[results++] = tag;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Getter
    private final int tagByte;

    @Getter
    private final int length;

    @Getter
    private String name;

    ConstantPoolTag(int tagByte, int length) {
        this.tagByte = tagByte;
        this.length = length;
    }

    @Override
    public String toString() {
        return name;
    }

    public static ConstantPoolTag<?> getTag(int tagByte) {
        for (var tag : values())
            if (tag.tagByte == tagByte)
                return tag;
        return null;
    }

    public static ConstantPoolTag<?>[] values() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstantPoolTag<?> that = (ConstantPoolTag<?>) o;
        return tagByte == that.tagByte;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagByte);
    }
}
