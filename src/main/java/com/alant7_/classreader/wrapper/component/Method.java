package com.alant7_.classreader.wrapper.component;

import com.alant7_.classreader.wrapper.WrapHelper;
import com.alant7_.classreader.io.RawClass;
import com.alant7_.classreader.io.RawMethod;
import com.alant7_.classreader.io.ComponentType;
import com.alant7_.classreader.io.ConstantPoolTag;
import com.alant7_.classreader.wrapper.abstracts.Component;
import com.alant7_.classreader.wrapper.component.type.TypeImpl;
import com.alant7_.classreader.wrapper.enums.AccessFlag;
import com.alant7_.classreader.wrapper.enums.Visibility;
import lombok.Getter;

import java.util.Arrays;

public class Method extends Component {

    @Getter
    private final TypeImpl returnType;

    @Getter
    private final TypeImpl[] parameterTypes;

    @Getter
    private final Parameter[] parameters;

    public Method(RawClass rawClass, RawMethod rawMethod, boolean wrapAnnotations) {
        super(
                rawClass,
                ComponentType.METHOD,
                rawClass.constantPool().entry(ConstantPoolTag.UTF8, rawMethod.nameIndex()).value(),
                rawMethod.accessFlags(),
                rawMethod.attributes(),
                wrapAnnotations
        );
        this.parameters = WrapHelper.getMethodParameters(rawClass, rawMethod, this, wrapAnnotations);
        this.returnType = WrapHelper.getMethodReturnType(rawClass, rawMethod);
        this.parameterTypes = Arrays.stream(parameters).map(Parameter::getType).toArray(TypeImpl[]::new);
    }

    public boolean isAbstract() {
        return hasAccessFlag(AccessFlag.ABSTRACT);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();

        if (getVisibility() != Visibility.PACKAGE) builder.append(getVisibility().name().toLowerCase() + " ");
        if (isAbstract()) builder.append("abstract ");
        builder.append(returnType.getSimpleName()).append(" ");
        builder.append(getName());
        builder.append("(");

        builder.append(String.join(", ", Arrays.stream(parameters).map(parameter -> parameter.getType().getSimpleName()).toArray(String[]::new)));

        builder.append(")");
        return builder.toString();
    }

}
