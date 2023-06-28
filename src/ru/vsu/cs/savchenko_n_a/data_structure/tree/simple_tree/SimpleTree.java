package ru.vsu.cs.savchenko_n_a.data_structure.tree.simple_tree;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SimpleTree<T> implements ITree<T> {

    private T fromStr(String s) {
        s = s.trim();
        if (s.length() > 0 && s.charAt(0) == '"') {
            s = s.substring(1);
        }
        if (s.length() > 0 && s.charAt(s.length() - 1) == '"') {
            s = s.substring(0, s.length() - 1);
        }
        if (fromStrFunc == null) {
            throw new RuntimeException("Не определена функция конвертации строки в T");
        }
        return fromStrFunc.apply(s);
    }

    private Node<T> root;

    @Override
    public Node<T> getRoot() {
        return root;
    }

    protected Function<String, T> fromStrFunc;
    protected Function<T, String> toStrFunc;

    public SimpleTree(Function<String, T> fromStrFunc, Function<T, String> toStrFunc) {
        this.fromStrFunc = fromStrFunc;
        this.toStrFunc = toStrFunc;
    }

    public SimpleTree(Function<String, T> fromStrFunc) {
        this(fromStrFunc, Object::toString);
    }

    public SimpleTree() {
        this(null);
    }

    public void fromBracketNotation(String bracketStr) throws Exception {
        IndexWrapper iw = new IndexWrapper();
        Node<T> rootNode = fromBracketStr(bracketStr, iw);
        if (iw.index < bracketStr.length()) {
            throw new RuntimeException(String.format("Ожидался конец строки [%d]", iw.index));
        }
        this.root = rootNode;
    }

    private static class Node<T> implements ITree.Node<T> {
        private final T value;
        private final List<ITree.Node<T>> children;
        private Color color;

        public Node(T value, List<ITree.Node<T>> children) {
            this.value = value;
            this.children = children;
            this.color = Color.WHITE;
        }

        public Node(T value) {
            this(value, new ArrayList<>());
        }

        @Override
        public T getValue() {
            return value;
        }

        public List<ITree.Node<T>> getChildren() {
            return children;
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
            while (bracketStr.charAt(iw.index) != ')') {
                if (bracketStr.charAt(iw.index) != ',') {
                    parentNode.children.add(fromBracketStr(bracketStr, iw));
                    skipSpaces(bracketStr, iw);
                }
                if (bracketStr.charAt(iw.index) == ',') {
                    iw.index++;
                    skipSpaces(bracketStr, iw);
                }
            }
            iw.index++;
        }
        return parentNode;
    }

    private static class IndexWrapper {
        private int index = 0;
    }

}
