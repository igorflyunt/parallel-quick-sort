package com.sandbox.lti.quicksort;

import com.sandbox.lti.quicksort.parallel.ParallelQuickSorter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class QuickSort {

    private QuickSort() {}

    public static void parallelSort(int[] data) {
        new ParallelQuickSorter(data).invoke();
    }

    public static void sort(int[] data) {
        serialSort(data);
    }

    public static void parallelSort(int[][] data) {
        parallelSort(data, FJQuickSortTaskAccumulator::new);
    }

    private static void parallelSort(int[][] data, Supplier<FJQuickSortTaskAccumulator> accuSupplier) {
        parallelSort(data, QuickSort::iterate, accuSupplier);
    }

    public static void sort(int[][] data) {
        iterate(data, QuickSort::serialSort);
    }

    public static void parallelSort(int[][][] data) {
        parallelSort(data, FJQuickSortTaskAccumulator::new);
    }

    private static void parallelSort(int[][][] data, Supplier<FJQuickSortTaskAccumulator> accuSupplier) {
        parallelSort(data, QuickSort::iterate, accuSupplier);
    }

    public static void sort(int[][][] data) {
        iterate(data, QuickSort::serialSort);
    }

    private static void serialSort(int[] data) {
        QuickSorter.sort(data, 0, data.length - 1);
    }

    private static <T> void parallelSort(T data, BiConsumer<T, Consumer<int[]>> itrConsumer,
                                         Supplier<FJQuickSortTaskAccumulator> accuSupplier) {
        var accu = accuSupplier.get();
        itrConsumer.accept(data, accu::accumulate);
        ForkJoinTask.invokeAll(accu.getTasks());
    }

    private static void iterate(int[][][] data, Consumer<int[]> consumer) {
        for (int[][] arr2d : data)
            iterate(arr2d, consumer);
    }

    private static void iterate(int[][] data, Consumer<int[]> consumer) {
        for (int[] els : data)
            consumer.accept(els);
    }

    private static class FJQuickSortTaskAccumulator {

        private final List<ForkJoinTask<int[]>> tasks = new ArrayList<>();

        public void accumulate(int[] data) {
            tasks.add(new ParallelQuickSorter(data));
        }

        public List<ForkJoinTask<int[]>> getTasks() {
            return tasks;
        }
    }
}
