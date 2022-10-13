package com.alant7_.classreader.wrapper.abstracts;

import java.lang.annotation.Annotation;

public interface Annotatable {

    boolean isAnnotationPresent(Class<? extends Annotation> annotation);

    <T extends Annotation> T getAnnotation(Class<T> annotation);

    // TODO: Get all annotations
//    Annotation getAnnotations();

    // TODO: Get all annotations of specified type
//    <T extends Annotation> T[] getAnnotations(Class<T> annotation);

}
