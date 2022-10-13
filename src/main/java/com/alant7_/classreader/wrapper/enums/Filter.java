package com.alant7_.classreader.wrapper.enums;

import java.lang.annotation.Annotation;

public class Filter<V> {

    public static final Filter<Class<? extends Annotation>> ANNOTATIONS = new Filter<>();

    private Filter() {}

}
