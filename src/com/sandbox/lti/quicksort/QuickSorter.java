package com.sandbox.lti.quicksort;

import static com.sandbox.lti.quicksort.QuickSortHelper.partition;

class QuickSorter {
    public static void sort(int[] data, int left, int right) {
        if (left < right) {
            int pivot = partition(data, left, right);
            sort(data, left, pivot);
            sort(data, pivot + 1, right);
        }
    }
}
