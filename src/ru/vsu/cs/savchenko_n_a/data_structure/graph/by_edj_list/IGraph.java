package ru.vsu.cs.savchenko_n_a.data_structure.graph.by_edj_list;

/**
 * Интерфейс для описания неориентированного графа (н-графа)
 * с реализацией некоторых методов графа
 */
public interface IGraph {
    /**
     * @return кол-во вершин в графе
     */
    int vertexQuantity();

    /**
     * @return кол-во ребер в графе
     */
    int edgeQuantity();

    /**
     * Добавление ребра между вершинами с номерами v1 и v2
     *
     * @param v1 вершина от
     * @param v2 вершина до
     */
    void addEdge(int v1, int v2);

    /**
     * Удаление ребра/ребер между вершинами с номерами v1 и v2
     *
     * @param v1 вершина от
     * @param v2 вершина до
     */
    void removeEdge(int v1, int v2);

    /**
     * @param v Номер вершины, смежные с которой необходимо найти
     * @return Объект, поддерживающий итерацию по номерам связанных с v вершин
     */
    Iterable<Integer> adjacency(int v);

    /**
     * @param v1 первая вершина
     * @param v2 вторая вершина
     * @return межны ли вершины
     */
    default boolean isAdj(int v1, int v2) {
        for (Integer adj : adjacency(v1)) {
            if (adj == v2) {
                return true;
            }
        }
        return false;
    }
}
