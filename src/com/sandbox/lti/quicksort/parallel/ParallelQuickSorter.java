package com.sandbox.lti.quicksort.parallel;

import java.util.concurrent.CountedCompleter;

import static com.sandbox.lti.quicksort.QuickSortHelper.partition;

public class ParallelQuickSorter extends CountedCompleter<int[]> {
    private final int[] data;
    private final int left;
    private final int right;

    public ParallelQuickSorter(int[] data) {
        this.data = data;
        left = 0;
        right = data.length - 1;
    }

    private ParallelQuickSorter(CountedCompleter<int[]> parent, int[] data, int left, int right) {
        super(parent);
        this.data = data;
        this.left = left;
        this.right = right;
    }

    @Override
    public void compute() {
        if (left < right) {
            int pivot = partition(data, left, right);
            forkSubset(pivot);
            new ParallelQuickSorter(this, data, pivot + 1, right).compute();
        } else {
            propagateCompletion();
        }
    }

    private void forkSubset(int pivot) {
        addToPendingCount(1);
        new ParallelQuickSorter(this, data, left, pivot).fork();
    }
}
