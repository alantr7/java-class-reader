package com.alant7_.classreader.wrapper.builder;

import com.alant7_.classreader.ClassWrapper;
import com.alant7_.classreader.io.ComponentType;
import com.alant7_.classreader.io.RawClass;
import com.alant7_.classreader.wrapper.Configuration;
import com.alant7_.classreader.wrapper.enums.Attribute;
import com.alant7_.classreader.wrapper.enums.Filter;

import java.lang.annotation.Annotation;

public class ClassWrapperBuilder {

    private final ClassWrapper wrapper;

    private final Configuration configuration = new Configuration();

    public ClassWrapperBuilder() {
        this.wrapper = new ClassWrapper(configuration);
    }

    @SafeVarargs
    public final <V> ClassWrapperBuilder filter(ComponentType component, Filter<V> filter, V... filterValues) {
        configuration.addFilter(component, filter, filterValues);
        return this;
    }

    public ClassWrapperBuilder ignoreFields(boolean flag) {
        configuration.ignoreFields(flag);
        return this;
    }

    public ClassWrapperBuilder ignoreMethods(boolean flag) {
        configuration.ignoreMethods(flag);
        return this;
    }

    public ClassWrapperBuilder ignoreAnnotations(boolean flag) {
        configuration.ignoreAnnotations(true);
        return this;
    }

    public ClassWrapper build() {
        return wrapper;
    }

}
