package ru.vsu.cs.savchenko_n_a.mycollections;

public class Tests {
    public static void main(String[] args) {
        MySinglyLinkedList<Integer> integers = new MySinglyLinkedList<>();
        System.out.println("Empty list: "  + integers);
        integers.addFirst(1);
        System.out.println("Add first: "  + integers);
        integers.addLast(0);
        System.out.println("Add last: "  + integers);
        integers.add(2, 2);
        System.out.println("Add by index {2} value {2}: "  + integers);
        System.out.println("Get by index {2}: "  + integers.get(2));
        System.out.println("Head: "  + integers.getHead());
        System.out.println("Tail: "  + integers.getTail());
        integers.remove(2);
        System.out.println("Remove by index 2: " + integers);
        integers.removeLast();
        System.out.println("Remove last: " + integers);
        integers.removeFirst();
        System.out.println("Remove first: " + integers);


    }
}
