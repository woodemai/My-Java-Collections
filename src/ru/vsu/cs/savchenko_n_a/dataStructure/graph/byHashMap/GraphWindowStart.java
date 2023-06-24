package ru.vsu.cs.savchenko_n_a.dataStructure.graph.byHashMap;

import javax.swing.*;

public class GraphWindowStart {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new GraphWindow();
    }
}
