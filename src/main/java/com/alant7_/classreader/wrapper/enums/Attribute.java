package com.alant7_.classreader.wrapper.enums;

import lombok.Getter;

public enum Attribute {

    CONSTANT_VALUE("ConstantValue"),
    CODE("Code"),

    INNER_CLASSES("InnerClasses"),

    SIGNATURE("Signature"),
    DEPRECATED("Deprecated"),

    RUNTIME_VISIBLE_ANNOTATIONS("RuntimeVisibleAnnotations"),
    RUNTIME_INVISIBLE_ANNOTATIONS("RuntimeInvisibleAnnotations"),

    RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS("RuntimeVisibleParameterAnnotations"),
    RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS("RuntimeInvisibleParameterAnnotations"),

    RUNTIME_VISIBLE_TYPE_ANNOTATIONS("RuntimeVisibleTypeAnnotations"),
    RUNTIME_INVISIBLE_TYPE_ANNOTATIONS("RuntimeInvisibleTypeAnnotations"),

    METHOD_PARAMETERS("MethodParameters")

    ;

    @Getter
    private final String attributeName;

    Attribute(String attributeName) {
        this.attributeName = attributeName;
    }

    public static Attribute getAttribute(String name) {
        for (var attr : values())
            if (attr.attributeName.equalsIgnoreCase(name))
                return attr;
        return null;
    }

}
