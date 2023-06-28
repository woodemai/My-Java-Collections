package ru.vsu.cs.savchenko_n_a.data_structure.tree.binary_tree;

import java.awt.*;
import java.util.Iterator;

public interface IBinaryTree<T> extends Iterable<T> {

    interface Node<T> extends Iterable<T> {
        T getValue();

        default Node<T> getLeft() {
            return null;
        }

        default Node<T> getRight() {
            return null;
        }
        @Override
        default Iterator<T> iterator() {
            return new BinaryTreeIterator<>(this);
        }

        default Color getColor() {
            return Color.BLACK;
        }

        void setColor(Color color);

    }

    @Override
    default Iterator<T> iterator() {
        return this.getRoot().iterator();
    }

    Node<T> getRoot();
}