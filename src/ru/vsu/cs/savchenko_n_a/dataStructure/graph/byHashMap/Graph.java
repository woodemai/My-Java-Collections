package ru.vsu.cs.savchenko_n_a.dataStructure.graph.byHashMap;

import java.util.*;

/**
 * <h2>Граф</h2>
 * Этот класс описывает граф и методы для взаимодействия с ним.
 * Граф является {@link HashMap хэш-таблицей}, где ключом являются значение узлов, а самим значениеми таблицы являются {@link Node узлы графа}.
 *
 * @see Node Узел графа
 * @see Edge Ребро соединяющие узлы
 */
public class Graph {
    private final HashMap<Integer, Node> graph = new HashMap<>();
    private int nodeQuantity;
    private int edgeQuantity;

    public Graph(int[][] graphData) {
        this.edgeQuantity = 0;
        this.nodeQuantity = 0;
        createGraph(graphData);
    }

    public Graph(int[][] graphData, boolean withMinimumSpanningTree) {
        this.edgeQuantity = 0;
        this.nodeQuantity = 0;
        createGraphWithMinimumRoads(graphData);
    }

    /**
     * <h2>Узел</h2>
     * Этот класс описывает узел графа. <br/>
     * {@link Integer value} - значение узла;<br/>
     * {@link LinkedHashSet edges} - ребра, которые идут от этого графа;<br/>
     * {@link LinkedHashMap parents} - описывает родителей, то-есть узлы от которых и ребра по которым можно добраться к этому узлу
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

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    '}';
        }
    }

    /**
     * Запись описывает ребро, являющиеся {@link Comparable Comparable}.
     *
     * @param adjacentNode узел, к которому привязано это ребро, то-есть узел от которого это ребро идет
     * @param weight       вес ребра
     */
    private record Edge(Node adjacentNode, int weight) implements Comparable<Edge> {
        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Edge) obj;
            return Objects.equals(this.adjacentNode, that.adjacentNode) &&
                    this.weight == that.weight;
        }

        @Override
        public int compareTo(Edge compareEdge) {
            return this.weight - compareEdge.weight;
        }
    }

    /**
     * <h3>Добавление или получение узла</h3>
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
     * <h3>Создание графа из массив данных</h3>
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

    /**
     * <h3>Создание массива с минимальным количеством самых коротких дорог</h3>
     * Тоже самое, что и предыдущий метод createGraph, только теперь граф создается с минимальным количеством дорог, а сами дороги выбираются минимальной длины.
     *
     * @param graphData массив данных, в котором первый столбец значения узла от которого строить ребро,
     *                  второй столбец с которому и третий стоблец вес ребра, значение "-1" отсутсвие узла/ребра
     */
    public void createGraphWithMinimumRoads(int[][] graphData) {
        sortByWeight(graphData);
        HashSet<Node> visited = new HashSet<>();
        for (int[] row : graphData) {
            Node node = addOrGetNode(row[0]);
            Node adjacentNode = addOrGetNode(row[1]);
            if (adjacentNode == null || node == null || (visited.contains(node) && visited.contains(adjacentNode))) continue;
            visited.add(adjacentNode);
            visited.add(node);
            Edge edge = new Edge(adjacentNode, row[2]);
            edgeQuantity++;
            node.edges.add(edge);
            adjacentNode.parents.put(node, edge);
        }
    }

    /**
     * <h3>Сортировка ребер по весу</h3>
     * Сортировка массива входных данных по третьему столбцу (столбцу с индексом 2), используется встроенный метод sort и кастомных компоратор.
     *
     * @param graphData неотсортированный массив
     */
    private void sortByWeight(int[][] graphData) {
        Arrays.sort(graphData, Comparator.comparingInt(row -> row[2]));
    }

    public String toDotWeightedGraph() {
        StringBuilder sb = new StringBuilder();
        String newLine = System.lineSeparator();
        sb.append("strict graph").append(" {").append(newLine);
        for (Node node : graph.values()) {
            if (!node.edges.isEmpty()) {
                for (Edge edge : node.edges) {
                    sb.append(node.value).append(" -- ").append(edge.adjacentNode.value).append(String.format(" [weight=%d label=%d]", edge.weight, edge.weight)).append(newLine);
                }
            } else {
                sb.append(node.value).append(newLine);
            }

        }
        sb.append("}");
        return sb.toString();
    }

    public String toDotGraph() {
        StringBuilder sb = new StringBuilder();
        String newLine = System.lineSeparator();
        sb.append("strict graph").append(" {").append(newLine);
        for (Node node : graph.values()) {
            if (!node.edges.isEmpty()) {
                for (Edge edge : node.edges) {
                    sb.append(node.value).append(" -- ").append(edge.adjacentNode.value).append(newLine);
                }
            } else {
                sb.append(node.value).append(newLine);
            }

        }
        sb.append("}");
        return sb.toString();
    }

    public String toDotWeightedDigraph() {
        StringBuilder sb = new StringBuilder();
        String newLine = System.lineSeparator();
        sb.append("strict digraph").append(" {").append(newLine);
        for (Node node : graph.values()) {
            if (!node.edges.isEmpty()) {
                for (Edge edge : node.edges) {
                    sb.append(node.value).append(" -> ").append(edge.adjacentNode.value).append(String.format(" [weight=%d label=%d]", edge.weight, edge.weight)).append(newLine);
                }
            } else {
                sb.append(node.value).append(newLine);
            }

        }
        sb.append("}");
        return sb.toString();
    }

    public String toDotDigraph() {
        StringBuilder sb = new StringBuilder();
        String newLine = System.lineSeparator();
        sb.append("strict digraph").append(" {").append(newLine);
        for (Node node : graph.values()) {
            if (!node.edges.isEmpty()) {
                for (Edge edge : node.edges) {
                    sb.append(node.value).append(" -> ").append(edge.adjacentNode.value).append(newLine);
                }
            } else {
                sb.append(node.value).append(newLine);
            }

        }
        sb.append("}");
        return sb.toString();
    }
}
