package com.alant7_.classreader.wrapper.abstracts;

import com.alant7_.classreader.io.*;
import com.alant7_.classreader.wrapper.WrapHelper;
import com.alant7_.classreader.wrapper.component.annotation.AnnotationInfo;
import com.alant7_.classreader.wrapper.enums.AccessFlag;
import com.alant7_.classreader.wrapper.enums.Attribute;
import com.alant7_.classreader.wrapper.enums.Visibility;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public abstract class Component implements Annotatable {

    protected final ComponentType type;

    protected final RawClass raw;

    @Getter
    private final String name;

    private final int flags;

    @Getter
    private final AccessFlag[] accessFlags;

    @Getter
    private final AttributeInfo[] attributes;

    @Getter
    private final AnnotationInfo[] annotationInfos;

    public Component(RawClass raw, ComponentType type, String name, int flags, AttributeInfo[] attributes, boolean wrapAnnotations) {
        this.type = type;
        this.raw = raw;
        this.name = name;
        this.flags = flags;
        this.attributes = attributes;
        this.accessFlags = Arrays.stream(AccessFlag.values(type)).filter(this::hasAccessFlag).toArray(AccessFlag[]::new);
        this.annotationInfos = wrapAnnotations ? WrapHelper.getAnnotations(raw, getAttribute(Attribute.RUNTIME_VISIBLE_ANNOTATIONS)) : new AnnotationInfo[0];
    }

    public boolean hasAttribute(Attribute attribute) {
        return getAttribute(attribute) != null;
    }

    public AttributeInfo getAttribute(Attribute attribute) {
        for (var attr : attributes) {
            var attrName = raw.constantPool().entry(ConstantPoolTag.UTF8, attr.nameIndex).value();
            if (attrName.equals(attribute.getAttributeName()))
                return attr;
        }
        return null;
    }

    public Attribute[] getAttributeTypes() {
        var attributes = new Attribute[this.attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = Attribute.getAttribute(raw.constantPool().entry(ConstantPoolTag.UTF8, this.attributes[i].nameIndex).value());
        }
        return attributes;
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotation) {
        for (var annotationInfo : annotationInfos) {
            if (annotationInfo.getType().getFullName().equals(annotation.getName()))
                return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotation) {
        for (var annotationInfo : annotationInfos) {
            if (annotationInfo.getType().getFullName().equals(annotation.getName())) {
                try {
                    return WrapHelper.getAnnotation((Class<? extends T>) annotationInfo.getType().loadClass(), annotationInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Attempts to get all of component's annotations.
     * If there is an annotation of which the class is not present in the classpath, it will not be instantiated
     * nor present in the returned array.
     *
     * @return Array of this component's annotations
     */
    @Override
    @SuppressWarnings("unchecked")
    public Annotation[] getAnnotations() {
        Annotation[] annotations = new Annotation[this.annotationInfos.length];
        int count = 0;
        for (int i = 0; i < annotations.length; i++) {
            try {
                var annotation = WrapHelper.getAnnotation((Class<? extends Annotation>) annotationInfos[i].getType().loadClass(), this.annotationInfos[i]);
                if (annotation != null)
                    annotations[count++] = annotation;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        var finalArray = new Annotation[count];
        System.arraycopy(annotations, 0, finalArray, 0, count);

        return finalArray;
    }

    @Override @SuppressWarnings("unchecked")
    public Class<? extends Annotation>[] getAnnotationsTypes() {
        Class<? extends Annotation>[] annotationTypes = new Class[this.annotationInfos.length];
        int count = 0;

        for (int i = 0; i < annotationTypes.length; i++) {
            try {
                annotationTypes[count++] = (Class<? extends Annotation>) annotationInfos[i].getType().loadClass();
            } catch (Error | Exception e) {
                e.printStackTrace();
            }
        }

        var finalArray = new Class[count];
        System.arraycopy(annotationTypes, 0, finalArray, 0, count);

        return finalArray;
    }

    public boolean isFinal() {
        return hasAccessFlag(AccessFlag.FINAL);
    }

    public boolean hasAccessFlag(int flag) {
        return AccessFlag.hasAccessFlag(flags, flag);
    }

    public boolean hasAccessFlag(AccessFlag flag) {
        return hasAccessFlag(flag.getValue());
    }

    public Visibility getVisibility() {
        return hasAccessFlag(AccessFlag.PUBLIC) ? Visibility.PUBLIC
                : hasAccessFlag(AccessFlag.PRIVATE) ? Visibility.PRIVATE
                : hasAccessFlag(AccessFlag.PROTECTED) ? Visibility.PROTECTED
                : Visibility.PACKAGE;
    }

}
