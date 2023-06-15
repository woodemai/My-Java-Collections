package ru.vsu.cs.savchenko_n_a.dataStructure.tree.nonBinaryTree;


import java.util.List;

public class SimpleTree<T> implements ITree<T> {

    private static class Node<T> implements ITree.Node<T> {
        private T value;
        private List<ITree.Node<T>> children;

        public Node(T value, List<ITree.Node<T>> children) {
            this.value = value;
            this.children = children;
        }

        public Node(T value) {
            this(value, null);
        }

        @Override
        public T getValue() {
            return value;
        }

        public List<ITree.Node<T>> getChildren() {
            return children;
        }
    }

    private Node<T> root = null;

    @Override
    public Node<T> getRoot() {
        return root;
    }


}
