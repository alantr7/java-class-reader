package com.alant7_.classreader;

import com.alant7_.classreader.io.RawClass;
import com.alant7_.classreader.wrapper.Configuration;
import com.alant7_.classreader.wrapper.WrapHelper;
import com.alant7_.classreader.wrapper.builder.ClassWrapperBuilder;
import com.alant7_.classreader.wrapper.component.ClassInfo;

public class ClassWrapper {

    private final Configuration configuration;

    public ClassWrapper(Configuration configuration) {
        this.configuration = configuration;
    }

    public ClassInfo wrap(RawClass rawClass) {
        return WrapHelper.getClass(rawClass, configuration);
    }

    public static ClassWrapperBuilder newBuilder() {
        return new ClassWrapperBuilder();
    }

}
