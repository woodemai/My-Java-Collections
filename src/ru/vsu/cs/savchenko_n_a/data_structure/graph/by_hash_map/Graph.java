package ru.vsu.cs.savchenko_n_a.data_structure.graph.by_hash_map;

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
    private static final String STRICT_GRAPH = "strict graph";
    private static final String STRICT_DIGRAPH = "strict digraph";
    private final HashMap<Integer, Node> graphHashMap = new HashMap<>();

    public Graph(int[][] graphData) {
        createGraph(graphData);
    }

    public Graph(int[][] graphData, boolean withMinimumSpanningTree) {
        if (withMinimumSpanningTree) {
            createGraphWithMinimumRoads(graphData);
        } else {
            createGraph(graphData);
        }

    }

    /**
     * <h3>Добавление или получение узла</h3>
     *
     * @param value значение узла
     * @return если узел найден в графе возвращает его, если не найден, то создает новый узел и возвращает его
     */
    private Node addOrGetNode(int value) {
        if (value == -1) return null;
        if (graphHashMap.containsKey(value)) return graphHashMap.get(value);
        Node node = new Node(value);
        graphHashMap.put(value, node);
        return node;
    }

    /**
     * <h3>Создание графа из массив данных</h3>
     *
     * @param graphData массив данных, в котором первый столбец значения узла от которого строить ребро,
     *                  второй столбец с которому и третий стоблец вес ребра, значение "-1" отсутсвие узла/ребра
     */
    public void createGraph(int[][] graphData) {
        for (int[] row : graphData) {
            Node node = addOrGetNode(row[0]);
            Node adjacentNode = addOrGetNode(row[1]);
            if (adjacentNode == null || node == null) continue;
            Edge edge = new Edge(adjacentNode, row[2]);
            node.getEdges().add(edge);
            adjacentNode.getParents().put(node, edge);
        }
    }

    /**
     * <h3>Список самых коротких дорог</h3>
     * Выводит список ребер, которые составляет вместе систему путей минимальной длины, соединяющие все узлы
     */
    public List<Edge> findMinimumRoads() {
        ArrayList<Edge> finalRoads = new ArrayList<>();
        Set<Node> visited = new HashSet<>();
        PriorityQueue<Edge> queue = new PriorityQueue<>();
        Node startNode = graphHashMap.values().iterator().next();
        queue.addAll(startNode.getEdges());
        int count = 0;
        while (!queue.isEmpty() && count < graphHashMap.size()) {
            Edge edge = queue.poll();
            Node adjacentNode = edge.adjacentNode();
            if (!visited.contains(adjacentNode)) {
                visited.add(adjacentNode);
                count++;
                finalRoads.add(edge);
                queue.addAll(adjacentNode.getEdges());
            }
        }
        return finalRoads;
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
            if (adjacentNode == null || node == null || (visited.contains(node) && visited.contains(adjacentNode)))
                continue;
            visited.add(adjacentNode);
            visited.add(node);
            Edge edge = new Edge(adjacentNode, row[2]);
            node.getEdges().add(edge);
            adjacentNode.getParents().put(node, edge);
        }
    }

    /**
     * <h3>Язык Dot - <strong>обычный</strong> граф</h3>
     *
     * @return граф в языке Dot
     */
    public String toDotGraph() {
        StringBuilder sb = new StringBuilder();
        String newLine = System.lineSeparator();
        sb.append(STRICT_GRAPH).append(" {").append(newLine);
        for (Node node : graphHashMap.values()) {
            if (!node.getEdges().isEmpty()) {
                for (Edge edge : node.getEdges()) {
                    sb.append(node.value).append(" -- ").append(edge.adjacentNode.value).append(newLine);
                }
            } else {
                sb.append(node.value).append(newLine);
            }

        }
        sb.append("}");
        return sb.toString();
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

    /**
     * <h3>Язык Dot - <strong>взвешенный</strong> граф</h3>
     *
     * @return граф в языке Dot
     */
    public String toDotWeightedGraph() {
        StringBuilder sb = new StringBuilder();
        String newLine = System.lineSeparator();
        sb.append(STRICT_GRAPH).append(" {").append(newLine);
        for (Node node : graphHashMap.values()) {
            if (!node.getEdges().isEmpty()) {
                for (Edge edge : node.getEdges()) {
                    sb.append(node.value).append(" -- ").append(edge.adjacentNode.value).append(String.format(" [weight=%d label=%d]", edge.weight, edge.weight)).append(newLine);
                }
            } else {
                sb.append(node.value).append(newLine);
            }

        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * <h3>Язык Dot - <strong>направленный</strong> граф</h3>
     *
     * @return граф в языке Dot
     */
    public String toDotDigraph() {
        StringBuilder sb = new StringBuilder();
        String newLine = System.lineSeparator();
        sb.append(STRICT_DIGRAPH).append(" {").append(newLine);
        for (Node node : graphHashMap.values()) {
            if (!node.getEdges().isEmpty()) {
                for (Edge edge : node.getEdges()) {
                    sb.append(node.value).append(" -> ").append(edge.adjacentNode.value).append(newLine);
                }
            } else {
                sb.append(node.value).append(newLine);
            }

        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * <h3>Язык Dot - <strong>взвешенный направленный</strong> граф</h3>
     *
     * @return граф в языке Dot
     */
    public String toDotWeightedDigraph() {
        StringBuilder sb = new StringBuilder();
        String newLine = System.lineSeparator();
        sb.append(STRICT_DIGRAPH).append(" {").append(newLine);
        for (Node node : graphHashMap.values()) {
            if (!node.getEdges().isEmpty()) {
                for (Edge edge : node.getEdges()) {
                    sb.append(node.value).append(" -> ").append(edge.adjacentNode.value).append(String.format(" [weight=%d label=%d]", edge.weight, edge.weight)).append(newLine);
                }
            } else {
                sb.append(node.value).append(newLine);
            }

        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * <h3>Язык Dot - <strong>взвешенный</strong> граф с выделением дорог</h3>
     * Помимо всего прочего, здесь выделены особым цветом ребра, которые передаются в списки на входе
     *
     * @param selected Ребра которые необходимо выделить
     * @return Граф в языке Dot
     */
    public String toDotWithImportantRoads(List<Edge> selected) {
        StringBuilder sb = new StringBuilder();
        String newLine = System.lineSeparator();
        sb.append(STRICT_GRAPH).append(" {").append(newLine);
        for (Node node : graphHashMap.values()) {
            if (!node.getEdges().isEmpty()) {
                for (Edge edge : node.getEdges()) {
                    if (selected.contains(edge)) {
                        sb.append(node.value).append(" -- ").append(edge.adjacentNode().value).append(String.format(" [weight=%d label=%d color=red]", edge.weight(), edge.weight()));
                    } else if (!selected.contains(node.getParents().get(edge.adjacentNode()))) {
                        sb.append(node.value).append(" -- ").append(edge.adjacentNode().value).append(String.format(" [weight=%d label=%d color=gray]", edge.weight(), edge.weight()));
                    }
                    sb.append(newLine);
                }
            } else {
                sb.append(node.getValue()).append(newLine);
            }

        }
        sb.append("}");
        return sb.toString();
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

        public Set<Edge> getEdges() {
            return edges;
        }

        public Map<Node, Edge> getParents() {
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

        @Override
        public String toString() {
            return "Edge[" +
                    "adjacentNode=" + adjacentNode + ", " +
                    "weight=" + weight + ']';
        }

    }
}
