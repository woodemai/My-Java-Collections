package ru.vsu.cs.savchenko_n_a.dataStructure.graph;

import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class GraphUtils {

    public static Graph fromStr(String str) throws IOException {
        Graph graph = new Graph();
        Map<String, Integer> names = new HashMap<>();
        int vertexCount;
        if (Pattern.compile("^\\s*(strict\\s+)?(graph|digraph)\\s*\\{").matcher(str).find()) {
            // dot-формат
            MutableGraph g = new Parser().read(str);
            vertexCount = g.nodes().size();
            graph.addEdge(vertexCount - 1, vertexCount - 1);
            graph.removeEdge(vertexCount - 1, vertexCount - 1);

            // проверка, являются ли все вершины целыми (-2 - не являются)
            Pattern intPattern = Pattern.compile("^\\d+$");
            int maxVertex = -1;
            for (Link l : g.links()) {
                String fromStr = Objects.requireNonNull(l.from()).toString();
                if (intPattern.matcher(fromStr).matches()) {
                    maxVertex = Math.max(maxVertex, Integer.parseInt(fromStr));
                } else {
                    maxVertex = -2;
                    break;
                }
                String toStr = l.from().toString();
                if (intPattern.matcher(toStr).matches()) {
                    maxVertex = Math.max(maxVertex, Integer.parseInt(toStr));
                } else {
                    maxVertex = -2;
                    break;
                }
            }
            vertexCount = 0;
            for (Link l : g.links()) {
                String fromStr = Objects.requireNonNull(l.from()).toString();
                Integer from;
                if (maxVertex == -2) {
                    from = names.get(fromStr);
                    if (from == null) {
                        from = vertexCount;
                        names.put(fromStr, from);
                        vertexCount++;
                    }
                } else {
                    from = Integer.parseInt(fromStr);
                }
                String toStr = l.to().toString();
                Integer to;
                if (maxVertex == -2) {
                    to = names.get(toStr);
                    if (to == null) {
                        to = vertexCount;
                        names.put(toStr, to);
                        vertexCount++;
                    }
                } else {
                    to = Integer.parseInt(toStr);
                }
                graph.addEdge(from, to);
            }
        } else if (Pattern.compile("^\\s*\\d+").matcher(str).find()) {
            Scanner scanner = new Scanner(str);
            int edgeCount = scanner.nextInt();
            for (int i = 0; i < edgeCount; i++) {
                graph.addEdge(scanner.nextInt(), scanner.nextInt());
            }
        } else {
            Scanner scanner = new Scanner(str);
            vertexCount = scanner.nextInt();
            while (scanner.hasNext()) {
                String fromStr = scanner.next();
                Integer from = names.get(fromStr);
                if (from == null) {
                    from = vertexCount;
                    names.put(fromStr, from);
                    vertexCount++;
                }
                String toStr = scanner.next();
                Integer to = names.get(toStr);
                if (to == null) {
                    to = vertexCount;
                    names.put(toStr, to);
                    vertexCount++;
                }
                graph.addEdge(from, to);
            }
        }

        return graph;
    }



    public static String toDot(Graph graph) {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        sb.append("strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < graph.vertexQuantity(); v1++) {
            int count = 0;
            for (Integer v2 : graph.adjacency(v1)) {
                sb.append(String.format("  %d %s %d", v1, "--", v2)).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }
    public static String toDotWithShortestCycle(Graph graph, List<Integer> shortestCycle) {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        sb.append("strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < graph.vertexQuantity(); v1++) {
            int count = 0;
            for (Integer v2 : graph.adjacency(v1)) {
                sb.append(String.format("  %d %s %d", v1, "--", v2)).append(nl);
                if (shortestCycle.contains(v2) && shortestCycle.contains(v1)) sb.append(" [color=red]");
                count++;
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }
}
