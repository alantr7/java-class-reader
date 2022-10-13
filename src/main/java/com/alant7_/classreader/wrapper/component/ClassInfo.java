package com.alant7_.classreader.wrapper.component;

import com.alant7_.classreader.io.RawClass;
import com.alant7_.classreader.io.ComponentType;
import com.alant7_.classreader.wrapper.abstracts.Component;
import lombok.Getter;

public class ClassInfo extends Component {

    @Getter
    private final String packageName;

    @Getter
    private final Field[] fields;

    @Getter
    private final Method[] methods;

    public ClassInfo(RawClass raw, String packageName, String name, Field[] fields, Method[] methods, boolean wrapAnnotations) {
        super(raw, ComponentType.CLASS, name, raw.accessFlags(), raw.attributes(), wrapAnnotations);
        this.packageName = packageName;
        this.fields = fields;
        this.methods = methods;
    }

}
