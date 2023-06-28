package ru.vsu.cs.savchenko_n_a.data_structure.map;

import javax.swing.*;

public class MapWindowStart {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new MapWindow();
    }
}
