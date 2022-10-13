package com.alant7_.classreader.wrapper.component.annotation;

import com.alant7_.classreader.wrapper.component.type.Type;
import com.alant7_.classreader.wrapper.component.type.TypeImpl;
import lombok.Getter;

public class AnnotationParameter {

    public enum ValueType {
        PRIMITIVE, CLASS, ENUM, ANNOTATION, ARRAY
    }

    @Getter
    private final ValueType valueType;

    @Getter
    public final Type type;

    @Getter
    public final Object value;

    public AnnotationParameter(ValueType valueType, TypeImpl type, Object value) {
        this.valueType = valueType;
        this.type = type;
        this.value = value;
    }

}
