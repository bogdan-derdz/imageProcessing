package org.umcs;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicArray {
    private int size;
    private AtomicIntegerArray atomicIntegerArray;

    public AtomicArray(int size) {
        this.size = size;
        this.atomicIntegerArray = new AtomicIntegerArray(size);
    }

    public int get(int index) {
        return atomicIntegerArray.get(index);
    }

    public void increment(int index) {
        atomicIntegerArray.incrementAndGet(index);
    }

    public int[] toArray() {
        int[] array = new int[size];

        for(int i = 0; i < atomicIntegerArray.length(); i++) {
            array[i] = atomicIntegerArray.get(i);
        }

        return array;
    }
}
