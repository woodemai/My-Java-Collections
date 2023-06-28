package ru.vsu.cs.savchenko_n_a.dataStructure.map;

import java.util.Set;
import java.util.TreeSet;

public class SimpleTreeMap<K extends Comparable<K>, V> {
    private Node<K, V> root;

    private static class Node<K, V> {
        K key;
        V value;

        public V getValue() {
            return value;
        }

        Node<K, V> left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private Node<K, V> put(Node<K, V> node, K key, V value) {
        if (node == null) {
            return new Node<>(key, value);
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
        Node<K, V> node = get(root, key);
        return node == null ? null : node.getValue();
    }

    private Node<K, V> get(Node<K, V> node, K key) {
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
        return keySet(root, new TreeSet<>());
    }

    private Set<K> keySet(Node<K, V> node, Set<K> keySet) {
        if (node == null) {
            return keySet;
        }

        keySet(node.left, keySet);
        keySet.add(node.key);
        keySet(node.right, keySet);

        return keySet;
    }

}