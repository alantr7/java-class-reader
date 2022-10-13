package com.alant7_.classreader.io;

public record RawField(int accessFlags, int nameIndex, int descriptorIndex, AttributeInfo[] attributes) {
}
