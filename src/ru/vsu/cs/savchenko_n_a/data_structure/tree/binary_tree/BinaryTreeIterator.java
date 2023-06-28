package ru.vsu.cs.savchenko_n_a.data_structure.tree.binary_tree;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BinaryTreeIterator<T> implements Iterator<T> {
    private final Stack<IBinaryTree.Node<T>> stack;

    public BinaryTreeIterator(IBinaryTree.Node<T> root) {
        stack = new Stack<>();
        if (root != null) {
            addLeftNodesToStack(root);
        }
    }

    private void addLeftNodesToStack(IBinaryTree.Node<T> node) {
        while (node != null) {
            stack.push(node);
            node = node.getLeft();
        }
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements in the tree.");
        }
        IBinaryTree.Node<T> current = stack.pop();
        T value = current.getValue();

        addLeftNodesToStack(current.getRight());
        return value;
    }
}
