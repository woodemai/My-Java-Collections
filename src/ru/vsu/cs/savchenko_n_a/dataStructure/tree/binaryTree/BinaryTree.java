package ru.vsu.cs.savchenko_n_a.dataStructure.tree.binaryTree;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;

public class BinaryTree<T> implements IBinaryTree<T> {
    private static class Node<T> implements IBinaryTree.Node<T> {
        private final T value;
        private Node<T> left;
        private Node<T> right;
        private Color color;

        public Node(T value, Node<T> left, Node<T> right) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.color = Color.WHITE;
        }

        public Node(T value) {
            this(value, null, null);
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public Node<T> getLeft() {
            return left;
        }

        @Override
        public Node<T> getRight() {
            return right;
        }
        @Override
        public Color getColor() {
            return color;
        }

        @Override
        public void setColor(Color color) {
            this.color = color;
        }
    }

    private Node<T> root;

    @Override
    public Node<T> getRoot() {
        return root;
    }

    protected Function<String, T> fromStrFunc;
    protected Function<T, String> toStrFunc;

    public BinaryTree(Function<String, T> fromStrFunc, Function<T, String> toStrFunc) {
        this.fromStrFunc = fromStrFunc;
        this.toStrFunc = toStrFunc;
    }

    public BinaryTree(Function<String, T> fromStrFunc) {
        this(fromStrFunc, Object::toString);
    }

    public BinaryTree() {
        this(null);
    }

    private static class IndexWrapper {
        public int index = 0;
    }

    private T fromStr(String s) throws Exception {
        s = s.trim();
        if (s.length() > 0 && s.charAt(0) == '"') {
            s = s.substring(1);
        }
        if (s.length() > 0 && s.charAt(s.length() - 1) == '"') {
            s = s.substring(0, s.length() - 1);
        }
        if (fromStrFunc == null) {
            throw new Exception("Не определена функция конвертации строки в T");
        }
        return fromStrFunc.apply(s);
    }

    private void skipSpaces(String bracketStr, IndexWrapper iw) {
        while (iw.index < bracketStr.length() && Character.isWhitespace(bracketStr.charAt(iw.index))) {
            iw.index++;
        }
    }

    private T readValue(String bracketStr, IndexWrapper iw) throws Exception {
        skipSpaces(bracketStr, iw);
        if (iw.index >= bracketStr.length()) {
            return null;
        }
        int from = iw.index;
        boolean quote = bracketStr.charAt(iw.index) == '"';
        if (quote) {
            iw.index++;
        }
        while (iw.index < bracketStr.length() && (
                quote && bracketStr.charAt(iw.index) != '"' ||
                        !quote && !Character.isWhitespace(bracketStr.charAt(iw.index)) && "(),".indexOf(bracketStr.charAt(iw.index)) < 0
        )) {
            iw.index++;
        }
        if (quote && bracketStr.charAt(iw.index) == '"') {
            iw.index++;
        }
        String valueStr = bracketStr.substring(from, iw.index);
        T value = fromStr(valueStr);
        skipSpaces(bracketStr, iw);
        return value;
    }

    private Node<T> fromBracketStr(String bracketStr, IndexWrapper iw) throws Exception {
        T parentValue = readValue(bracketStr, iw);
        Node<T> parentNode = new Node<>(parentValue);
        if (bracketStr.charAt(iw.index) == '(') {
            iw.index++;
            skipSpaces(bracketStr, iw);
            if (bracketStr.charAt(iw.index) != ',') {
                parentNode.left = fromBracketStr(bracketStr, iw);
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) == ',') {
                iw.index++;
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) != ')') {
                parentNode.right = fromBracketStr(bracketStr, iw);
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) != ')') {
                throw new Exception(String.format("Ожидалось ')' [%d]", iw.index));
            }
            iw.index++;
        }

        return parentNode;
    }

    public void fromBracketNotation(String bracketStr) throws Exception {
        IndexWrapper iw = new IndexWrapper();
        Node<T> root = fromBracketStr(bracketStr, iw);
        if (iw.index < bracketStr.length()) {
            throw new Exception(String.format("Ожидался конец строки [%d]", iw.index));
        }
        this.root = root;
    }

    public List<Integer> findLevelsWithMaxElements() {
        List<Integer> maxLevelList = new ArrayList<>();

        if (root == null) {
            return maxLevelList;
        }
        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(root);
        int currentLevel = -1;
        int maxSize = 1;
        maxLevelList.add(currentLevel);

        while (!queue.isEmpty()) {
            currentLevel++;
            int levelSize = queue.size();
            if (levelSize == maxSize) {
                maxLevelList.add(currentLevel);
            } else if (levelSize > maxSize) {
                maxSize = levelSize;
                maxLevelList.clear();
                maxLevelList.add(currentLevel);
            }
            for (int i = 0; i < levelSize; i++) {
                Node<T> node = queue.poll();
                if (node != null) {
                    if (node.getLeft() != null) {
                        queue.add(node.getLeft());
                    }
                    if (node.getRight() != null) {
                        queue.add(node.getRight());
                    }
                }
            }
        }
        return maxLevelList;
    }
    public void colorLevelsWithMaxElements(List<Integer> maxLevels) {
        if (root == null || maxLevels.size() == 0) {
            return;
        }
        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(root);
        int currentLevel = -1;
        while (!queue.isEmpty()) {
            currentLevel++;
            int levelSize = queue.size();
            boolean contains = maxLevels.contains(currentLevel);
            for (int i = 0; i < levelSize; i++) {
                Node<T> node = queue.poll();
                if (node != null) {
                    if (contains) {
                        node.setColor(Color.BLACK);
                    }
                    if (node.getLeft() != null) {
                        queue.add(node.getLeft());
                    }
                    if (node.getRight() != null) {
                        queue.add(node.getRight());
                    }
                }
            }
        }
    }
}
