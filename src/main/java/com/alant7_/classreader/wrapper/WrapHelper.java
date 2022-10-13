package com.alant7_.classreader.wrapper;

import com.alant7_.classreader.io.*;
import com.alant7_.classreader.wrapper.component.*;
import com.alant7_.classreader.wrapper.component.annotation.AnnotationInfo;
import com.alant7_.classreader.wrapper.component.annotation.AnnotationInvocationHandler;
import com.alant7_.classreader.wrapper.component.annotation.AnnotationParameter;
import com.alant7_.classreader.wrapper.component.type.PrimitiveType;
import com.alant7_.classreader.wrapper.component.type.Type;
import com.alant7_.classreader.wrapper.component.type.TypeImpl;
import com.alant7_.classreader.wrapper.enums.Attribute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WrapHelper {

    private static final Map<Character, ConstantPoolTag<?>> annotationTags = new HashMap<>();

    private static final Map<Character, PrimitiveType<?>> primitiveTags = new HashMap<>();

    static {
        char[] CONSTANT_Integer = {'B', 'C', 'I', 'S', 'Z'};
        for (char ch : CONSTANT_Integer) {
            annotationTags.put(ch, ConstantPoolTag.INTEGER);
        }

        annotationTags.put('D', ConstantPoolTag.DOUBLE);
        annotationTags.put('F', ConstantPoolTag.FLOAT);
        annotationTags.put('J', ConstantPoolTag.LONG);
        annotationTags.put('s', ConstantPoolTag.UTF8);

        primitiveTags.put('D', PrimitiveType.DOUBLE);
        primitiveTags.put('F', PrimitiveType.FLOAT);
        primitiveTags.put('I', PrimitiveType.INTEGER);
        primitiveTags.put('J', PrimitiveType.LONG);

        primitiveTags.put('B', PrimitiveType.BYTE);
        primitiveTags.put('C', PrimitiveType.CHAR);
        primitiveTags.put('S', PrimitiveType.SHORT);
        primitiveTags.put('Z', PrimitiveType.BOOLEAN);
    }

    public static Parameter[] getMethodParameters(RawClass rawClass, RawMethod rawMethod, Method method, boolean wrapAnnotations) {
        String descriptor = rawClass.constantPool().entry(ConstantPoolTag.UTF8, rawMethod.descriptorIndex()).value();
        String[] parametersString = descriptor.substring(1).substring(0, descriptor.indexOf(')') - 1).split(";");

        int parametersCount = (int) Arrays.stream(parametersString).filter(s -> s != null && s.length() > 0).count();
        var parameters = new Parameter[parametersCount];

        var parameterAnnotations = wrapAnnotations && method.hasAttribute(Attribute.RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS)
                ? getParameterAnnotations(rawClass, method.getAttribute(Attribute.RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS).info, new AtomicInteger(0))
                : new AnnotationInfo[parametersCount][];

        for (int i = 0; i < parametersCount; i++) {
            String parameterString = parametersString[i];
            if (parameterString == null || parameterString.length() == 0)
                continue;

            var parameterType = TypeImpl.of(parameterString);
            parameters[i] = new Parameter(parameterType, parameterAnnotations[i]);
        }

        return parameters;
    }

    public static TypeImpl getMethodReturnType(RawClass rawClass, RawMethod rawMethod) {
        String descriptor = rawClass.constantPool().entry(ConstantPoolTag.UTF8, rawMethod.descriptorIndex()).value();
        return TypeImpl.of(descriptor.substring(descriptor.indexOf(')') + 1));
    }

    public static ClassInfo getClass(RawClass rawClass, Configuration configuration) {
        var pool = rawClass.constantPool();
        var fullName = pool.entry(ConstantPoolTag.UTF8, pool.entry(ConstantPoolTag.INTEGER, rawClass.thisClassIndex()).value()).value();
        var fields = new Field[configuration.ignoreFields() ? 0 : rawClass.fields().length];
        var methods = new Method[configuration.ignoreMethods() ? 0 : rawClass.methods().length];

        int packageAndNameDivider = fullName.lastIndexOf('/');
        var packageName = packageAndNameDivider != -1 ? fullName.substring(0, packageAndNameDivider).replace('/', '.') : "";
        var name = packageAndNameDivider != -1 ? fullName.substring(packageAndNameDivider + 1) : fullName;

        for (int i = 0; i < fields.length; i++) {
            fields[i] = new Field(rawClass, rawClass.fields()[i], !configuration.ignoreAnnotations());
        }

        for (int i = 0; i < methods.length; i++) {
            methods[i] = new Method(rawClass, rawClass.methods()[i], !configuration.ignoreAnnotations());
        }

        return new ClassInfo(rawClass, packageName, name, fields, methods, !configuration.ignoreAnnotations());
    }

    public static AnnotationInfo[] getAnnotations(RawClass rawClass, AttributeInfo attribute) {
        if (attribute == null)
            return new AnnotationInfo[0];

        int annotationsCount = ByteHelper.toInt(attribute.info, 0, 2);
        var index = new AtomicInteger(2);

        var annotations = new AnnotationInfo[annotationsCount];

        for (int i = 0; i < annotationsCount; i++) {
            annotations[i] = getAnnotation(rawClass, attribute.info, index);
        }

        return annotations;
    }

    /**
     * Creates an instance of {@link Annotation} and maps all of its values
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotation(Class<T> annotationClass, AnnotationInfo annotationInfo) {
        var values = new HashMap<String, Object>();

        annotationInfo.getPairs().forEach((key, param) -> {
            var parameterValue = getAnnotationParameter(annotationClass, key, param);
            values.put(key, parameterValue);
        });

        try {
            return (T) Proxy.newProxyInstance(
                    annotationClass.getClassLoader(),
                    new Class[]{annotationClass},
                    new AnnotationInvocationHandler(annotationClass, values)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AnnotationInfo getAnnotation(RawClass rawClass, byte[] info, AtomicInteger index) {

        int typeIndex = ByteHelper.toInt(info, index.getAndAdd(2), 2);
        var annotationType = TypeImpl.of(rawClass.constantPool().entry(ConstantPoolTag.UTF8, typeIndex).value());

        int pairsCount = ByteHelper.toInt(info, index.getAndAdd(2), 2);
        var pairs = new HashMap<String, AnnotationParameter>();

        for (int j = 0; j < pairsCount; j++) {

            var pairKey = rawClass.constantPool().entry(ConstantPoolTag.UTF8, ByteHelper.toInt(info, index.getAndAdd(2), 2)).value();
            var pairValue = getAnnotationPairValue(rawClass, info, index);

            pairs.put(pairKey, pairValue);

        }

        return new AnnotationInfo(annotationType, pairs);

    }

    public static AnnotationInfo[][] getParameterAnnotations(RawClass rawClass, byte[] info, AtomicInteger index) {

        int num_parameters = ByteHelper.toInt(info, index.getAndAdd(1), 1);
        var parameter_annotations = new AnnotationInfo[num_parameters][];

        for (int i = 0; i < num_parameters; i++) {
            int num_annotations = ByteHelper.toInt(info, index.getAndAdd(2), 2);
            var annotations = new AnnotationInfo[num_annotations];

            for (int j = 0; j < num_annotations; j++) {
                annotations[j] = getAnnotation(rawClass, info, index);
            }

            parameter_annotations[i] = annotations;
        }

        return parameter_annotations;

    }

    public static AnnotationParameter getAnnotationPairValue(RawClass rawClass, byte[] info, AtomicInteger index) {

        // Read the value
        char tag = (char) ByteHelper.toInt(info, index.getAndAdd(1), 1);

        return switch (tag) {

            // Parses an enum value
            case 'e' -> {
                int type_name_index = ByteHelper.toInt(info, index.getAndAdd(2), 2);
                int const_name_index = ByteHelper.toInt(info, index.getAndAdd(2), 2);

                yield new AnnotationParameter(
                        AnnotationParameter.ValueType.ENUM,
                        TypeImpl.of(rawClass.constantPool().entry(ConstantPoolTag.UTF8, type_name_index).value()),
                        rawClass.constantPool().entry(ConstantPoolTag.UTF8, const_name_index).value());
            }


            // Parses a class reference
            case 'c' -> {
                int class_info_index = ByteHelper.toInt(info, index.getAndAdd(2), 2);
                yield new AnnotationParameter(
                        AnnotationParameter.ValueType.CLASS,
                        TypeImpl.of(rawClass.constantPool().entry(ConstantPoolTag.UTF8, class_info_index).value()),
                        TypeImpl.of(rawClass.constantPool().entry(ConstantPoolTag.UTF8, class_info_index).value()).getClassPath()
                );
            }


            // Parses an annotation value
            case '@' -> {
                var annotation = getAnnotation(rawClass, info, index);
                yield new AnnotationParameter(AnnotationParameter.ValueType.ANNOTATION, annotation.getType(), annotation);
            }


            // Reads an array value
            case '[' -> {

                int num_values = ByteHelper.toInt(info, index.getAndAdd(2), 2);
                Object[] array = new Object[num_values];

                Type type = null;

                for (int i = 0; i < num_values; i++) {
                    array[i] = getAnnotationPairValue(rawClass, info, index);
                    type = ((AnnotationParameter) array[i]).type;
                }

                yield new AnnotationParameter(AnnotationParameter.ValueType.ARRAY, (TypeImpl) type, array);
//                yield new AnnotationParameter() array;

            }


            // Parses a constant value, e.g. integer, boolean, etc. (primitive types)
            default -> {

                int const_value_index = ByteHelper.toInt(info, index.getAndAdd(2), 2);

                var cptag = annotationTags.get(tag);
                var type = primitiveTags.get(tag);

                var value = rawClass.constantPool().entry(cptag, const_value_index).value();

                if (tag == 'Z') {
                    yield new AnnotationParameter(AnnotationParameter.ValueType.PRIMITIVE, type, ((int) value) == 1);
                }

                yield new AnnotationParameter(AnnotationParameter.ValueType.PRIMITIVE, type, value);

            }

        };

    }

    @SuppressWarnings("unchecked")
    public static Object getAnnotationParameter(Class<?> annotationClass, String name, AnnotationParameter parameter) {
        try {
            return switch (parameter.getValueType()) {
                case CLASS -> parameter.type.loadClass();
                case ENUM -> Enum.valueOf((Class<Enum>) parameter.type.loadClass(), (String) parameter.value);
                case PRIMITIVE -> parameter.value;
                case ANNOTATION ->
                        getAnnotation((Class<? extends Annotation>) parameter.type.loadClass(), (AnnotationInfo) parameter.value);
                case ARRAY -> {

                    var parameterValue = (Object[]) parameter.value;
                    var annotationMethod = annotationClass.getMethod(name);

                    var returnType = annotationMethod.getReturnType();
                    var array = (Object[]) Array.newInstance(returnType.componentType(), parameterValue.length);

                    for (int i = 0; i < parameterValue.length; i++) {
                        array[i] = getAnnotationParameter(annotationClass, name, (AnnotationParameter) parameterValue[i]);
                    }

                    yield annotationMethod.getReturnType().cast(array);

                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
