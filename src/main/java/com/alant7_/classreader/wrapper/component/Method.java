package com.alant7_.classreader.wrapper.component;

import com.alant7_.classreader.wrapper.WrapHelper;
import com.alant7_.classreader.io.RawClass;
import com.alant7_.classreader.io.RawMethod;
import com.alant7_.classreader.io.ComponentType;
import com.alant7_.classreader.io.ConstantPoolTag;
import com.alant7_.classreader.wrapper.abstracts.Component;
import com.alant7_.classreader.wrapper.component.type.TypeImpl;
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
        this.parameters = WrapHelper.getMethodParameters(rawClass, rawMethod);
        this.returnType = WrapHelper.getMethodReturnType(rawClass, rawMethod);
        this.parameterTypes = Arrays.stream(parameters).map(Parameter::getType).toArray(TypeImpl[]::new);
    }

}
