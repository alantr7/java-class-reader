package com.alant7_.classreader.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ConstantPoolTagResolver {

    private final Map<ConstantPoolTag<?>, ResolveFunction> resolvers = new HashMap<>();

    public ConstantPoolTagResolver() {
        prepare();
    }

    private void prepare() {
        // utf8: 1
        resolvers.put(ConstantPoolTag.UTF8, (stream, bytes) -> new String(stream.readNBytes(ByteHelper.readU2(bytes)), StandardCharsets.UTF_8));

        // integer: 3
        resolvers.put(ConstantPoolTag.INTEGER, (stream, bytes) -> ByteHelper.toInt(bytes));
        resolvers.put(ConstantPoolTag.FLOAT, (stream, bytes) -> {
            var buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
            return buffer.getFloat();
        });
        resolvers.put(ConstantPoolTag.LONG, (stream, bytes) -> {
            var buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
            return buffer.getLong();
        });
        resolvers.put(ConstantPoolTag.DOUBLE, (stream, bytes) -> {
            var buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
            return buffer.getDouble();
        });


        // references
        resolvers.put(ConstantPoolTag.CLASS_REFERENCE, (stream, bytes) -> ByteHelper.readU2(bytes));
        resolvers.put(ConstantPoolTag.STRING_REFERENCE, (stream, bytes) -> ByteHelper.readU2(bytes));
        resolvers.put(ConstantPoolTag.FIELD_REFERENCE, (stream, bytes) -> new int[] { ByteHelper.toInt(bytes, 0, 2), ByteHelper.toInt(bytes, 2, 2) });
        resolvers.put(ConstantPoolTag.METHOD_REFERENCE, (stream, bytes) -> new int[] { ByteHelper.toInt(bytes, 0, 2), ByteHelper.toInt(bytes, 2, 2) });
        resolvers.put(ConstantPoolTag.INTERFACE_METHOD_REFERENCE, (stream, bytes) -> new int[] { ByteHelper.toInt(bytes, 0, 2), ByteHelper.toInt(bytes, 2, 2) });

        // descriptor: 12
        resolvers.put(ConstantPoolTag.NAME_TYPE_DESCRIPTOR, (stream, bytes) -> new int[] { ByteHelper.toInt(bytes, 0, 2), ByteHelper.toInt(bytes, 2, 2) });

        resolvers.put(ConstantPoolTag.METHOD_HANDLE, (stream, bytes) -> new int[] { ByteHelper.toInt(bytes, 0, 1), ByteHelper.toInt(bytes, 1, 2) });
        resolvers.put(ConstantPoolTag.METHOD_TYPE, (stream, bytes) -> ByteHelper.readU2(bytes));

        resolvers.put(ConstantPoolTag.DYNAMIC, (stream, bytes) -> bytes);
        resolvers.put(ConstantPoolTag.INVOKE_DYNAMIC, (stream, bytes) -> bytes);
    }

    public <T> Object resolve(ConstantPoolTag<T> tag, InputStream stream, byte[] bytes) {
        try {
            return resolvers.get(tag).resolve(stream, bytes);
        } catch (Exception ignored) {
            System.out.println("Could not find resolver for tag: " + tag);
            return null;
        }
    }

    @FunctionalInterface
    interface ResolveFunction {

        Object resolve(InputStream stream, byte[] bytes) throws IOException;

    }

}
