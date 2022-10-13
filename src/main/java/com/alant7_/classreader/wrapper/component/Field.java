package com.alant7_.classreader.wrapper.component;

import com.alant7_.classreader.io.*;
import com.alant7_.classreader.wrapper.abstracts.Component;
import com.alant7_.classreader.wrapper.component.type.TypeImpl;
import com.alant7_.classreader.wrapper.enums.Visibility;
import lombok.Getter;

import java.util.Arrays;

public class Field extends Component {

    @Getter
    private final TypeImpl type;

    public Field(RawClass raw, RawField rawField, boolean wrapAnnotations) {
        super(
                raw,
                ComponentType.FIELD,
                raw.constantPool().entry(ConstantPoolTag.UTF8, rawField.nameIndex()).value(),
                rawField.accessFlags(),
                rawField.attributes(),
                wrapAnnotations
        );
        this.type = TypeImpl.of(raw.constantPool().entry(ConstantPoolTag.UTF8, rawField.descriptorIndex()).value());
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();

        if (getVisibility() != Visibility.PACKAGE) builder.append(getVisibility().name().toLowerCase() + " ");
        if (isFinal()) builder.append("final ");
        builder.append(getType().getSimpleName()).append(" ");
        builder.append(getName());

        return builder.toString();
    }

}
