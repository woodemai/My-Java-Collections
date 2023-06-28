package ru.vsu.cs.savchenko_n_a.dataStructure.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordsNTimesMethods {
    // SimpleTreeMap
    private static SimpleTreeMap<String, Integer> simpleArrayToMap(String[] arr) {
        SimpleTreeMap<String, Integer> map = new SimpleTreeMap<>();
        for (String current : arr) {
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

    private static ArrayList<String> findN(SimpleTreeMap<String, Integer> map, int n) {
        ArrayList<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            if (map.get(key) == n) list.add(key);
        }
        return list;
    }

    public static ArrayList<String> simpleGetWords(String str, int n) {
        String[] arr = convertStringToArray(str);
        SimpleTreeMap<String, Integer> map = simpleArrayToMap(arr);
        return findN(map, n);
    }

    //TreeMap
    public static String[] convertStringToArray(String string) {
        ArrayList<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char current = string.charAt(i);
            if (Character.isLetter(current)) {
                sb.append(current);
            } else if (sb.length() > 0) {
                list.add(sb.toString());
                sb = new StringBuilder();
            }
        }
        return list.toArray(new String[0]);
    }

    private static Map<String, Integer> arrayToMap(String[] arr) {
        Map<String, Integer> map = new HashMap<>(arr.length);
        for (String current : arr) {
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

    private static ArrayList<String> findN(Map<String, Integer> map, int n) {
        ArrayList<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            if (map.get(key) == n) list.add(key);
        }
        return list;
    }

    public static ArrayList<String> getWords(String str, int n) {
        String[] arr = convertStringToArray(str);
        Map<String, Integer> map = arrayToMap(arr);
        return findN(map, n);
    }
}
