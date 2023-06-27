package ru.vsu.cs.savchenko_n_a.dataStructure.map;

import java.util.HashSet;
import java.util.Set;

public class SimpleTreeMap<K extends Comparable<K>, V> {
    private Node root;

    private class Node {
        K key;
        V value;
        Node left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private Node put(Node node, K key, V value) {
        if (node == null) {
            return new Node(key, value);
        }

        if (compare(key, node.key) < 0) {
            node.left = put(node.left, key, value);
        } else if (compare(key, node.key) > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
        }

        return node;
    }

    public V get(K key) {
        Node node = get(root, key);
        return node == null ? null : node.value;
    }

    private Node get(Node node, K key) {
        if (node == null) {
            return null;
        }

        if (compare(key, node.key) < 0) {
            return get(node.left, key);
        } else if (compare(key, node.key) > 0) {
            return get(node.right, key);
        } else {
            return node;
        }
    }
    public boolean containsKey(K key) {
        return get(root, key) != null;
    }

    private int compare(K key1, K key2) {
        return key1.compareTo(key2);
    }
    public Set<K> keySet() {
        return keySet(root, new HashSet<>());
    }

    private Set<K> keySet(Node node, Set<K> keySet) {
        if (node == null) {
            return keySet;
        }

        keySet.add(node.key);
        keySet(node.left, keySet);
        keySet(node.right, keySet);

        return keySet;
    }
}