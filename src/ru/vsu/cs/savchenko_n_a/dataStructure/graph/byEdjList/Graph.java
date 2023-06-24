package ru.vsu.cs.savchenko_n_a.dataStructure.graph.byEdjList;

import java.util.*;

/**
 * Реализация графа на основе списков смежности
 */
public class Graph implements IGraph {

    private final List<List<Integer>> vEdjLists = new ArrayList<>();
    private int vertexQuantity = 0;
    private int edgeQuantity = 0;



    private static final Iterable<Integer> nullIterable = () -> new Iterator<>() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Integer next() {
            return null;
        }
    };

    @Override
    public int vertexQuantity() {
        return vertexQuantity;
    }

    @Override
    public int edgeQuantity() {
        return edgeQuantity;
    }

    @Override
    public void addEdge(int v1, int v2) {
        int maxV = Math.max(v1, v2);
        for (; vertexQuantity <= maxV; vertexQuantity++) {
            vEdjLists.add(null);
        }
        if (!isAdj(v1, v2)) {
            if (vEdjLists.get(v1) == null) {
                vEdjLists.set(v1, new LinkedList<>());
            }
            vEdjLists.get(v1).add(v2);
            edgeQuantity++;
            if (vEdjLists.get(v2) == null) {
                vEdjLists.set(v2, new LinkedList<>());
            }
            vEdjLists.get(v2).add(v1);
        }
    }

    private int countingRemove(List<Integer> list, int v) {
        int count = 0;
        if (list != null) {
            for (Iterator<Integer> it = list.iterator(); it.hasNext(); ) {
                if (it.next().equals(v)) {
                    it.remove();
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void removeEdge(int v1, int v2) {
        edgeQuantity -= countingRemove(vEdjLists.get(v1), v2);
        edgeQuantity -= countingRemove(vEdjLists.get(v2), v1);
    }

    @Override
    public Iterable<Integer> adjacency(int v) {
        return vEdjLists.get(v) == null ? nullIterable : vEdjLists.get(v);
    }

    public List<Integer> findShortestCycle() {
        List<Integer> shortestCycle = new ArrayList<>();
        for (int i = 0; i < vertexQuantity; i++) {
            List<Integer> currentCycle = dfs(i);
            if ((currentCycle.size() < shortestCycle.size() && currentCycle.size() > 2) || shortestCycle.isEmpty()) {
                shortestCycle = currentCycle;
            }
        }
        return shortestCycle;
    }

    private List<Integer> dfs(int startVertex) {
        Stack<Integer> stack = new Stack<>();
        int[] parent = new int[vertexQuantity];
        Arrays.fill(parent, -1);
        boolean[] visited = new boolean[vertexQuantity];
        stack.add(startVertex);
        while (!stack.isEmpty()) {
            int currentVertex = stack.pop();
            visited[currentVertex] = true;
            for (int neighbor : adjacency(currentVertex)) {
                if (neighbor == startVertex && parent[currentVertex] != startVertex) {
                    List<Integer> cycle = new LinkedList<>();
                    cycle.add(neighbor);
                    int vertex = currentVertex;
                    while (vertex != startVertex) {
                        cycle.add(vertex);
                        vertex = parent[vertex];
                    }
                    cycle.add(neighbor);
                    return cycle;
                }
                if (!visited[neighbor]) {
                    stack.add(neighbor);
                    parent[neighbor] = currentVertex;
                }
            }
        }
        return new ArrayList<>();
    }
}
