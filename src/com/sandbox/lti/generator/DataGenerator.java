package com.sandbox.lti.generator;

import java.util.List;
import java.util.Random;

public final class DataGenerator {
    public static final int SIZE_TEN = 10;
    public static final int SIZE_HUNDRED = SIZE_TEN * SIZE_TEN;
    public static final int SIZE_THOUSAND = SIZE_TEN * SIZE_HUNDRED;
    public static final int SIZE_TEN_THOUSAND = SIZE_TEN * SIZE_THOUSAND;
    public static final int SIZE_HUNDRED_THOUSAND = SIZE_TEN * SIZE_TEN_THOUSAND;
    public static final int SIZE_MILLION = SIZE_TEN * SIZE_HUNDRED_THOUSAND;
    public static final int SIZE_TEN_MILLION = SIZE_TEN * SIZE_MILLION;
    public static final int SIZE_HUNDRED_MILLION = SIZE_TEN * SIZE_TEN_MILLION;

    public static final List<Integer> SIZES = List.of(
            SIZE_TEN,
            SIZE_HUNDRED,
            SIZE_THOUSAND,
            SIZE_TEN_THOUSAND,
            SIZE_HUNDRED_THOUSAND,
            SIZE_MILLION,
            SIZE_TEN_MILLION,
            SIZE_HUNDRED_MILLION
    );
    
    private static final Random rand = new Random();
    
    private DataGenerator() {}

    public static int[][][] generateArray3D(int arraySize) {
        final int elsSize = arraySize / SIZE_HUNDRED;
        int[][][] arr = new int[SIZE_TEN][SIZE_TEN][elsSize];
        for (int i = 0; i < arr.length; i++)
            arr[i] = generateArray2D(SIZE_TEN * elsSize);
        return arr;
    }

    public static int[][] generateArray2D(int arraySize) {
        int[][] arr = new int[SIZE_TEN][arraySize / SIZE_TEN];
        for (int i = 0; i < arr.length; i++)
            arr[i] = generateArray1D(arr[i].length);
        return arr;
    }

    public static int[] generateArray1D(int arraySize) {
        return rand.ints(arraySize, 0, SIZE_TEN_MILLION)
                   .toArray();
    }
}
