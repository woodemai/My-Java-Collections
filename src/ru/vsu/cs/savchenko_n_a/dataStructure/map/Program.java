package ru.vsu.cs.savchenko_n_a.dataStructure.map;

import java.util.Map;
import java.util.TreeMap;

public class Program {
    public static void main(String[] args) {
        int[] arr = new int[]{5, 2, 5, 7, 2, 4, 7, 5, 4, 1, 2, 4};
        System.out.println("Array: ");
        showIntArr(arr);
        arr = sortIntArray(arr);
        System.out.println("Sorted: ");
        showIntArr(arr);
    }

    public static int[] sortIntArray(int[] arr) {
        Map<Integer, Integer> map = setMap(arr);
        return readMap(map, arr.length);
    }

    private static Map<Integer, Integer> setMap(int[] arr) {
        Map<Integer, Integer> map = new TreeMap<>();
        for (int current : arr) {
            if (map.containsKey(current)) {
                int quantity = map.get(current);
                quantity++;
                map.put(current, quantity);
            } else {
                map.put(current, 1);
            }
        }
        return map;
    }

    private static int[] readMap(Map<Integer, Integer> map, int arrLength) {
        int[] sortedArr = new int[arrLength];
        int index = 0;
        for (int key : map.keySet()) {
            int quantity = map.get(key);
            for (int i = 0; i < quantity; i++) {
                sortedArr[index] = key;
                index++;
            }
        }
        return sortedArr;
    }
    public static int[] myMapSortIntArray(int[] arr) {
        SimpleTreeMap<Integer, Integer> map = myMapSetMap(arr);
        return myMapReadMap(map, arr.length);
    }

    private static int[] myMapReadMap(SimpleTreeMap<Integer, Integer> map, int length) {
        int[] sortedArr = new int[length];
        int index = 0;
        for (int key : map.keySet()) {
            int quantity = map.get(key);
            for (int i = 0; i < quantity; i++) {
                sortedArr[index] = key;
                index++;
            }
        }
        return sortedArr;
    }

    private static SimpleTreeMap<Integer, Integer> myMapSetMap(int[] arr) {
        SimpleTreeMap<Integer, Integer> map = new SimpleTreeMap<>();
        for (int current : arr) {
            if (map.containsKey(current)) {
                int quantity = map.get(current);
                quantity++;
                map.put(current, quantity);
            } else {
                map.put(current, 1);
            }
        }
        return map;
    }

    private static void showIntArr(int[] arr) {
        for (int value : arr) {
            System.out.print(value + " ");
        }
        System.out.println();
    }
}
