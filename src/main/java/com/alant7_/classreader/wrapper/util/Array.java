package com.alant7_.classreader.wrapper.util;

import java.util.Arrays;
import java.util.function.IntFunction;

@SuppressWarnings("unchecked")
public class Array<E> {

    private final Object[] array;

    private int size = 0;

    public Array(int capacity) {
        this.array = new Object[capacity];
    }

    public Array(E[] elements) {
        this.array = elements;
        this.size = array.length;
    }

    public Array(E[] elements, E fillValue) {
        this(elements);
        Arrays.fill(array, fillValue);
    }

    /**
     * Adds an element to the end of the array
     * @param element Element to be added
     */
    public void push(E element) {
        if (size == array.length)
            return;

        array[size++] = element;
    }

    /**
     * Removes the last element of the array
     * @return Element that is removed, or {@code null} if the array is empty
     */
    public E pop() {
        if (size == 0)
            return null;

        var element = array[--size];
        array[size] = null;

        return (E) element;
    }

    /**
     * Removes the first element of the array
     * @return Element that is removed, or {@code null} if the array is empty
     */
    public E shift() {
        if (size == 0)
            return null;

        var element = array[0];
        System.arraycopy(array, 1, array, 0, size);

        array[--size] = null;
        return (E) element;
    }

    /**
     * Adds an element to the beginning of the array
     * @param element Element to be added
     */
    public void unshift(E element) {
        if (size == array.length)
            return;

        System.arraycopy(array, 0, array, 1, size);
        size++;

        array[0] = element;
    }

    public E[] getArray() {
        Object[] newArray = new Object[size];
        System.arraycopy(array, 0, newArray, 0, size);

        return (E[]) newArray;
    }

    public static <E> Array<E> of(E... elements) {
        return new Array<>(elements);
    }

}
