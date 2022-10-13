package com.alant7_.classreader.wrapper.component;

import com.alant7_.classreader.wrapper.abstracts.Annotatable;
import com.alant7_.classreader.wrapper.component.annotation.AnnotationInfo;
import com.alant7_.classreader.wrapper.component.type.TypeImpl;
import lombok.Getter;

import java.lang.annotation.Annotation;

public class Parameter implements Annotatable {

    @Getter
    private final TypeImpl type;

    @Getter
    private final AnnotationInfo[] annotations;

    public Parameter(TypeImpl type, AnnotationInfo[] annotations) {
        this.type = type;
        this.annotations = annotations;
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotation) {
        return getAnnotation(annotation) != null;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotation) {

        return null;
    }

}
