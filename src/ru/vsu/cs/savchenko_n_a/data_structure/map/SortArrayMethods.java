package ru.vsu.cs.savchenko_n_a.data_structure.map;

import java.util.Map;
import java.util.TreeMap;

public class SortArrayMethods {
    private SortArrayMethods() {
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
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            for (int i = 0; i < value; i++) {
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
}
