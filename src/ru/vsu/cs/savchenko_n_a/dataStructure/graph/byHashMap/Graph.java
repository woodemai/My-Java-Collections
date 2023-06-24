package ru.vsu.cs.savchenko_n_a.dataStructure.graph.byHashMap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * описывает граф и методы для взаимодействия
 */
public class Graph {
    private final HashMap<Integer, Node> graph = new HashMap<>();
    private int nodeQuantity;
    private int edgeQuantity;

    public int getNodeQuantity() {
        return nodeQuantity;
    }

    public int getEdgeQuantity() {
        return edgeQuantity;
    }

    public Graph(int[][] graphData) {
        this.edgeQuantity = 0;
        this.nodeQuantity = 0;
        createGraph(graphData);
    }

    /**
     * описывает узел графа
     * value - значение узла
     * edges - ребра, которые идут от этого графа
     * parents - описывает родителей, то-есть узлы от которых идут ребра к этому узлу.
     * это хэш-таблица, в которой ключ - узел (родитель), значение ребро (от родителя к нашему узлу)
     */
    private static class Node {
        private final int value;
        private final LinkedHashSet<Edge> edges = new LinkedHashSet<>();
        private final LinkedHashMap<Node, Edge> parents = new LinkedHashMap<>();

        public int getValue() {
            return value;
        }

        public LinkedHashSet<Edge> getEdges() {
            return edges;
        }

        public LinkedHashMap<Node, Edge> getParents() {
            return parents;
        }

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * Запись описывает ребро
     *
     * @param adjacentNode узел, к которому привязано это ребро, то-есть ребро от которого это ребро идет
     * @param weight       вес ребра
     */
    private record Edge(Node adjacentNode, int weight) {
    }

    /**
     * @param value значение узла
     * @return если узел найден в графе возвращает его, если не найден, то создает новый узел и возвращает его
     */
    private Node addOrGetNode(int value) {
        if (value == -1) return null;
        if (graph.containsKey(value)) return graph.get(value);
        Node node = new Node(value);
        graph.put(value, node);
        nodeQuantity++;
        return node;
    }

    /**
     * @param graphData массив данных, в котором первый столбец значения узла от которого строить ребро,
     *                  второй столбец с которому и третий стоблец вес ребра, значение "-1" отсутсвие узла/ребра
     */
    public void createGraph(int[][] graphData) {
        for (int[] row : graphData) {
            Node node = addOrGetNode(row[0]);
            if (node == null) continue;
            Node adjacentNode = addOrGetNode(row[1]);
            if (adjacentNode == null) continue;
            Edge edge = new Edge(adjacentNode, row[2]);
            edgeQuantity++;
            node.edges.add(edge);
            adjacentNode.parents.put(node, edge);
        }
    }

    public String toDot() {
        StringBuilder sb = new StringBuilder();
        String newLine = System.lineSeparator();
        sb.append("strict digraph").append(" {").append(newLine);
        for (Node node : graph.values()) {
            if (!node.edges.isEmpty()) {
                sb.append(node.value).append(" -> {");
                for (Edge edge : node.edges) {
                    sb.append(edge.adjacentNode.value).append(" ");
                }
                sb.append("}").append(newLine);
            }

        }
        sb.append("}");
        return sb.toString();
    }

}
