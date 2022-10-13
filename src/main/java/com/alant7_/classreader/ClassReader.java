package com.alant7_.classreader;

import com.alant7_.classreader.io.*;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class ClassReader {

    private final InputStream stream;

    private RawClass raw;

    @SneakyThrows
    public ClassReader(File file) {
        this.stream = new FileInputStream(file);
    }

    public ClassReader(InputStream stream) {
        this.stream = stream;
    }
    public RawClass read() {
        return raw != null ? raw : (raw = _readClassFile());
    }

    private RawClass _readClassFile() {
        int[] magic = _readMagicNumber();
        int minor = ByteHelper.readU2(stream);
        int major = ByteHelper.readU2(stream);
        var constantPool = _readConstantPool();
        int accessFlags = ByteHelper.readU2(stream);
        int thisClass = ByteHelper.readU2(stream);
        int superClass = ByteHelper.readU2(stream);
        int[] interfaces = _readInterfaces();
        var fields = _readFields();
        var methods = _readMethods();
        var attributes = _readAttributes();

        try {
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new RawClass(magic, minor, major, constantPool, accessFlags, thisClass, superClass, interfaces, fields, methods, attributes);
    }

    private int[] _readMagicNumber() {
        int[] magic = new int[4];
        for (int i = 0; i < magic.length; i++) {
            magic[i] = ByteHelper.readU1(stream);
        }

        return magic;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private ConstantPool _readConstantPool() {
        byte[] constantPoolCount = stream.readNBytes(2);

        int length = ByteHelper.readU2(constantPoolCount) - 1;
        var pool = new ConstantPool(length + 1);
        var resolver = pool.getTagResolver();

        Map<ConstantPoolTag<?>, Integer> stats = new HashMap<>();

        for (int i = 0; i < length; i++) {
            // read tag
            int tagByte = ByteHelper.readU1(stream);

            var tag = ConstantPoolTag.getTag(tagByte);
            if (tag != null) {

                byte[] bytes = stream.readNBytes(tag.getLength());

                var resolved = resolver.resolve(tag, stream, bytes);
                if (resolved != null) {
                    var entry = new ConstantPool.Entry<>((ConstantPoolTag<Object>) tag, resolved);
                    pool.put(i + 1, entry);

                    stats.put(tag, stats.getOrDefault(tag, 0) + 1);

                    if (tag == ConstantPoolTag.DOUBLE || tag == ConstantPoolTag.LONG) {
                        pool.put(i + 2, entry);
                        i++;
                    }
                } else {
                    System.out.println((i + 1) + ".\t\t/" + length + "\tCould not resolve tag: " + tag);
                }

            } else {
                System.out.println("Unknown Constant Pool Tag: " + tagByte);
            }

        }

        return pool;
    }

    private int[] _readInterfaces() {
        int count = ByteHelper.readU2(stream);
        int[] indices = new int[count];

        for (int i = 0; i < count; i++) {
            int index = ByteHelper.readU2(stream);
            indices[i] = index;
        }

        return indices;
    }

    private RawField[] _readFields() {
        int count = ByteHelper.readU2(stream);
        var fields = new RawField[count];

        for (int i = 0; i < count; i++) {
            int accessFlags = ByteHelper.readU2(stream);
            int nameIndex = ByteHelper.readU2(stream);
            int descriptorIndex = ByteHelper.readU2(stream);
            int attributesCount = ByteHelper.readU2(stream);

            var attributes = new AttributeInfo[attributesCount];
            for (int j = 0; j < attributes.length; j++) {
                attributes[j] = _readAttribute();
            }

            fields[i] = new RawField(accessFlags, nameIndex, descriptorIndex, attributes);
        }

        return fields;
    }

    private RawMethod[] _readMethods() {
        int count = ByteHelper.readU2(stream);
        var methods = new RawMethod[count];

        for (int i = 0; i < methods.length; i++) {
            int accessFlags = ByteHelper.readU2(stream);
            int nameIndex = ByteHelper.readU2(stream);
            int descriptorIndex = ByteHelper.readU2(stream);
            int attributesCount = ByteHelper.readU2(stream);

            var attributes = new AttributeInfo[attributesCount];
            for (int j = 0; j < attributes.length; j++) {
                attributes[j] = _readAttribute();
            }

            methods[i] = new RawMethod(accessFlags, nameIndex, descriptorIndex, attributes);
        }

        return methods;
    }

    private AttributeInfo[] _readAttributes() {
        int count = ByteHelper.readU2(stream);
        var attributes = new AttributeInfo[count];

        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = _readAttribute();
        }

        return attributes;
    }

    @SneakyThrows
    private AttributeInfo _readAttribute() {
        int attributeNameIndex = ByteHelper.readU2(stream);
        int attributeLength = ByteHelper.readU4(stream);

        byte[] info = new byte[attributeLength];
        stream.read(info);

        return new AttributeInfo(attributeNameIndex, info);
    }

}
