package ru.vsu.cs.savchenko_n_a.dataStructure.tree.nonBinaryTree;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface ITree<T> extends Iterable<T> {

    interface Node<T> extends Iterable<T> {
        T getValue();

        default List<Node<T>> getChildren() {
            return new ArrayList<>();
        }
        @Override
        default Iterator<T> iterator() {
            return new SimpleTreeIterator<>(this);
        }
    }

    @Override
    default Iterator<T> iterator() {
        return this.getRoot().iterator();
    }

    Node<T> getRoot();

}
