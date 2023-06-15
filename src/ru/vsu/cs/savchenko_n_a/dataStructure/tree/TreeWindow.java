package ru.vsu.cs.savchenko_n_a.dataStructure.tree;

import ru.vsu.cs.savchenko_n_a.dataStructure.tree.windows.Window;

import javax.swing.*;

public class TreeWindow {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new Window();
    }
}
