package ru.vsu.cs.savchenko_n_a.collections.list;

import java.util.Iterator;
import java.util.Objects;

public class MySinglyLinkedList<T> implements Iterable<T> {

    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    private static class Node<T> {
        private final T value;
        private Node<T> next;

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }

        public Node(T value) {
            this(value, null);
        }

        public T getValue() {
            return value;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return value.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node<?> node)) return false;
            return Objects.equals(getValue(), node.getValue()) && Objects.equals(getNext(), node.getNext());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getValue(), getNext());
        }
    }

    class MyIterator implements Iterator<T> {
        Node<T> current;
        Node<T> prev;

        public MyIterator() {
            this.current = new Node<>(null, head);
            this.prev = null;
        }

        @Override
        public boolean hasNext() {
            return current.getNext() != null;
        }

        @Override
        public T next() {
            prev = current;
            current = current.getNext();
            return current.getValue();
        }

        @Override
        public void remove() {
            if (current == head) {
                head = head.getNext();
            }
            prev.setNext(current.getNext());
            if (prev.getValue() == null) {
                tail = prev;
            }
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public MySinglyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void add(int index, T value) {
        if (index == 0) {
            addFirst(value);
        } else if (index == size()) {
            addLast(value);
        } else {
            Node<T> prev = getNode(index - 1);
            Node<T> newNode = new Node<>(value, prev.getNext());
            prev.setNext(newNode);

        }
    }

    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value, head);
        if (isEmpty()) {
            tail = newNode;
        }
        head = newNode;
        size++;
    }

    public void addLast(T value) {
        if (isEmpty()) {
            addFirst(value);
            return;
        }
        Node<T> newNode = new Node<>(value);
        tail.next = newNode;
        tail = newNode;
        size++;
    }

    private Node<T> getNode(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Invalid index!");
        }
        if (isEmpty()) {
            throw new NullPointerException("List is empty!");
        }
        int counter = 0;
        Node<T> current = head;
        while (current != null && counter < index) {
            current = current.getNext();
            counter++;
        }
        if (current == null) {
            throw new NullPointerException("List corrupted exception");
        }
        return current;
    }

    public Node<T> getHead() {
        return getNode(0);
    }


    public Node<T> getTail() {
        return getNode(size() - 1);
    }

    public T get(int index) {
        return getNode(index).getValue();
    }

    public void remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Invalid index!");
        }
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("List is empty");
        }
        if (index == 0) {
            removeFirst();
        } else if (index == size() - 1) {
            removeLast();
        } else {
            if (size() == 1) {
                head = tail = null;
                return;
            }
            int counter = 0;
            Node<T> prev = null;
            Node<T> current = head;
            while (current != null && counter != index) {
                counter++;
                prev = current;
                current = current.next;
            }
            if (current == null) {
                throw new NullPointerException("List corrupted exception");
            }
            prev.next = current.next;
            size--;
        }
    }

    public T getFirst() {
        return getNode(0).getValue();
    }

    public T getLast() {
        return getNode(size() - 1).getValue();
    }

    public void removeFirst() {
        if (size() == 1) {
            head = tail = null;
            return;
        }
        head = head.getNext();
        size--;
    }

    public void removeLast() {
        if (size() == 1) {
            head = tail = null;
            return;
        }
        Node<T> current = head;
        while (current != null && current.next != tail) {
            current = current.getNext();
        }
        if (current == null) {
            throw new NullPointerException("List corrupted exception");
        }
        current.next = null;
        tail = current;
        size--;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node<T> current = head;
        while (current != null) {
            builder.append(current.getValue()).append("; ");
            current = current.getNext();
        }
        return builder.toString();
    }
}