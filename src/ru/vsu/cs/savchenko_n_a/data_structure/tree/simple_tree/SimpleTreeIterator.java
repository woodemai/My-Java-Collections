package ru.vsu.cs.savchenko_n_a.data_structure.tree.simple_tree;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

public class SimpleTreeIterator<T> implements Iterator<T> {
    private final Stack<ITree.Node<T>> stack;

    public SimpleTreeIterator(ITree.Node<T> root) {
        stack = new Stack<>();
        if (root != null) {
            stack.push(root);
        }
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements in the Tree");
        }
        ITree.Node<T> current = stack.pop();
        T value = current.getValue();
        List<ITree.Node<T>> children = current.getChildren();
        for (ITree.Node<T> child : children) {
            stack.push(child);
        }
        return value;
    }
}
