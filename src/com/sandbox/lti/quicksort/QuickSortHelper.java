package com.sandbox.lti.quicksort;

public final class QuickSortHelper {

    private QuickSortHelper() {}

    public static int partition(int[] array, int low, int high) {
        int pivot = array[(low + high) >>> 1];
        int i = low - 1;
        int j  = high + 1;
        while (true){
            do { i++; } while (array[i] < pivot);
            do { j--; } while (array[j] > pivot);
            if (i >= j) return j;
            swap(array, i, j);
        }
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
