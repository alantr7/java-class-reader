package com.alant7_.classreader.io;

import lombok.Getter;

public class ConstantPool {

    private final Entry<?>[] entries;

    @Getter
    private final ConstantPoolTagResolver tagResolver;

    public ConstantPool(int size) {
        this.entries = new Entry[size];
        this.tagResolver = new ConstantPoolTagResolver();
    }

    @SuppressWarnings("unchecked")
    public <V> Entry<V> entry(ConstantPoolTag<V> tag, int index) {
        return (Entry<V>) entries[index];
    }

    public <V> void put(int index, Entry<V> entry) {
        entries[index] = entry;
    }

    public record Entry<V>(ConstantPoolTag<V> tag, V value) {
    }

}
