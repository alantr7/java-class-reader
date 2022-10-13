package com.alant7_.classreader.io;

import lombok.SneakyThrows;

import java.io.InputStream;

public class ByteHelper {

    public static int toInt(byte[] bytes) {
        int result = 0;
        int index = 0;

        while (index < bytes.length) {
            result <<= 8;
            result = result | (bytes[index++] & 255);
        }

        return result;
    }

    public static int toInt(byte[] bytes, int offset, int length) {
        byte[] buffer = new byte[length];
        System.arraycopy(bytes, offset, buffer, 0, length);

        return toInt(buffer);
    }

    // TODO: Optimize!!!
    public static byte[] toBytes(long number) {
        int index = 0;
        int length = 0;
        long temp = number;

        while (temp > 0) {
            temp >>= 8;
            length++;
        }

        byte[] bytes = new byte[length];
        while (number > 0) {
            bytes[index++] = (byte) ((number & 255));
            number >>= 8;
        }

        byte[] reversed = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            reversed[bytes.length - 1 - i] = bytes[i];
        }

        return reversed;
    }

    public static byte[] toBytes(long number, int capacity) {
        var bytes = toBytes(number);
        if (bytes.length >= capacity)
            return bytes;

        int offset = capacity - bytes.length;
        var upgraded = new byte[capacity];
        System.arraycopy(bytes, 0, upgraded, offset, bytes.length);

        return upgraded;
    }

    public static int readU1(byte b) {
        return toInt(new byte[] { b });
    }

    @SneakyThrows
    public static int readU1(InputStream stream) {
        return readU1(stream.readNBytes(1)[0]);
    }

    public static int readU2(byte[] b) {
        assert b.length == 2;
        return toInt(b);
    }

    @SneakyThrows
    public static int readU2(InputStream stream) {
        return readU2(stream.readNBytes(2));
    }

    public static int readU4(byte[] b) {
        assert b.length == 4;
        return toInt(b);
    }

    @SneakyThrows
    public static int readU4(InputStream stream) {
        return readU4(stream.readNBytes(4));
    }

}
