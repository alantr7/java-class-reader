package com.alant7_.classreader.io;

public record RawMethod(int accessFlags, int nameIndex, int descriptorIndex, AttributeInfo[] attributes) {
}
