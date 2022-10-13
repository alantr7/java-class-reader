package com.alant7_.classreader.wrapper.component.annotation;

import com.alant7_.classreader.wrapper.component.type.TypeImpl;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

public class AnnotationInfo {

    @Getter
    private final TypeImpl type;

    @Getter
    private final Map<String, AnnotationParameter> pairs;

    public AnnotationInfo(TypeImpl type, Map<String, AnnotationParameter> pairs) {
        this.type = type;
        this.pairs = pairs;
    }

    @Override
    public String toString() {
        return "@" + type.getFullName() + "(" + String.join(", ", pairs.entrySet().stream().map(entry -> {
            return entry.getKey() + "=" + (entry.getValue().value.getClass().isArray()
                    ? Arrays.toString(Arrays.stream((Object[]) entry.getValue().value).map(i -> ((AnnotationParameter) i).value).toArray())
                    : entry.getValue().value);
        }).toArray(String[]::new)) + ")";
    }

}
