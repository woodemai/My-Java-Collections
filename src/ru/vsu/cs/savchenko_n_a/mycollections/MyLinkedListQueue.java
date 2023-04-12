package ru.vsu.cs.savchenko_n_a.mycollections;

public class MyLinkedListQueue<T> {

    private static class Node<T> {
        public T value;
        public Node<T> next;

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }

        public Node(T value) {
            this(value, null);
        }

        @Override
        public String toString() {
            return value.toString();
        }

    }

    private Node<T> head = null;
    private Node<T> tail = null;
    private int size = 0;

    public MyLinkedListQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public MyLinkedListQueue(T[] arr) {
        for (T value : arr) {
            add(value);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void add(T value) {
        if (size() == 0) {
            head = tail = new Node<>(value);
        } else {
            tail.next = new Node<>(value);
            tail = tail.next;
        }
        size++;
    }

    public T element() throws Exception {
        if (size() == 0) {
            throw new Exception("Queue is empty");
        }
        return head.value;
    }

    public T remove() throws Exception {
        T result = element();
        head = head.next;
        size--;
        if (size() == 0) {
            tail = null;
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;
        for (int i = 0; i < size; i++) {
            sb.append(current).append("\n");
            current = current.next;
        }
        return sb.toString();
    }
}