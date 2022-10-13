package com.alant7_.classreader.io;

public record RawClass(int[] magicNumber, int minorVersion, int majorVersion, ConstantPool constantPool,
                       int accessFlags, int thisClassIndex, int superClassIndex, int[] interfaceIndices,
                       RawField[] fields, RawMethod[] methods, AttributeInfo[] attributes) {

}
