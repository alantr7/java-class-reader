package com.alant7_.classreader.io;

public class AttributeInfo {

    public final int nameIndex;

    public final byte[] info;

    public AttributeInfo(int nameIndex, byte[] info) {
        this.nameIndex = nameIndex;
        this.info = info;
    }

}
