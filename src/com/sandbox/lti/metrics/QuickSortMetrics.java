package com.sandbox.lti.metrics;

import com.sandbox.lti.generator.DataGenerator;
import com.sandbox.lti.quicksort.QuickSort;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.sandbox.lti.generator.DataGenerator.*;
import static java.util.function.Predicate.not;

public class QuickSortMetrics {

    private enum Operation {
        SEQUENTIAL("sequential"),
        PARALLEL ("parallel");

        private final String name;

        Operation(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private enum ArrayType {
        ONE_DIMENSIONAL("One"),
        TWO_DIMENSIONAL("Two"),
        THREE_DIMENSIONAL("Three");

        private final String type;

        ArrayType(String type) {
            this.type = String.format("%s dimensional array", type);
        }

        @Override
        public String toString() {
            return type;
        }
    }

    private QuickSortMetrics() {}

    public static void testWithArray1D() {
        testWithArray(ArrayType.ONE_DIMENSIONAL, s -> false, QuickSortMetrics::testWithArray1D);
    }

    private static void testWithArray1D(int arraySize) {
        gaugeExecutionTime(arraySize, DataGenerator::generateArray1D,
                           QuickSort::sort, QuickSort::parallelSort);
    }

    public static void testWithArray2D() {
        testWithArray(ArrayType.TWO_DIMENSIONAL, not(s -> s.equals(SIZE_HUNDRED)),
                      QuickSortMetrics::testWithArray2D);
    }

    private static void testWithArray2D(int arraySize) {
        gaugeExecutionTime(arraySize, DataGenerator::generateArray2D,
                           QuickSort::sort, QuickSort::parallelSort);
    }

    public static void testWithArray3D() {
        testWithArray(ArrayType.THREE_DIMENSIONAL, not(s -> s.equals(SIZE_THOUSAND)),
                      QuickSortMetrics::testWithArray3D);
    }

    private static void testWithArray3D(int arraySize) {
        gaugeExecutionTime(arraySize, DataGenerator::generateArray3D,
                           QuickSort::sort, QuickSort::parallelSort);
    }

    private static void testWithArray(ArrayType type, Predicate<Integer> needsToSkip, Consumer<Integer> testConsumer) {
        System.out.format("[%s]\n", type);
        SIZES.stream()
             .dropWhile(needsToSkip)
             .forEach(size -> gaugeExecutionTime(testConsumer, size));
    }

    private static void gaugeExecutionTime(Consumer<Integer> testConsumer, Integer size) {
        testConsumer.accept(size);
        System.out.println();
    }

    private static <T> void gaugeExecutionTime(int size, Function<Integer, T> arrGenerator,
                                               Consumer<T> quickSorter, Consumer<T> fjQuickSorter) {
        outputSize(size);
        gaugeExecutionTime(Operation.SEQUENTIAL, quickSorter, arrGenerator.apply(size));
        gaugeExecutionTime(Operation.PARALLEL, fjQuickSorter, arrGenerator.apply(size));
    }

    private static <T> void gaugeExecutionTime(Operation operation, Consumer<T> quickSorter, T data) {
        long startTime = System.currentTimeMillis();
        quickSorter.accept(data);
        long executionTime = System.currentTimeMillis() - startTime;
        System.out.format("quick-sort.%s = %d ms\n", operation, executionTime);
    }

    private static void outputSize(int arraySize) {
        System.out.format("size = %d\n", arraySize);
    }
}
