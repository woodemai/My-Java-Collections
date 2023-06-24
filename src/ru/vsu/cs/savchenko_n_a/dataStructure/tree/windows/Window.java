package ru.vsu.cs.savchenko_n_a.dataStructure.tree.windows;

import ru.vsu.cs.savchenko_n_a.dataStructure.tree.binaryTree.BinaryTree;
import ru.vsu.cs.savchenko_n_a.dataStructure.tree.binaryTree.BinaryTreePainter;
import ru.vsu.cs.savchenko_n_a.dataStructure.tree.nonBinaryTree.SimpleTree;
import ru.vsu.cs.savchenko_n_a.dataStructure.tree.nonBinaryTree.SimpleTreePainter;
import ru.vsu.cs.savchenko_n_a.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Window extends JFrame {

    private JPanel contentPane;
    private JPanel panelPaintArea;
    private JTextField textFieldBrakedNotationBinaryTree;
    private JButton buttonBuildBrakedBinaryTree;
    private JButton buttonFindMaxElementsLevelBinaryTree;
    private JTextField textFieldBrakedNotationTree;
    private JButton buttonBuildBrakedTree;
    BinaryTree<Integer> binaryTree = new BinaryTree<>();
    SimpleTree<Integer> simpleTree = new SimpleTree<>();
    boolean binary = true;
    private final JPanel paintPanel;


    public Window() {
        setTitle("Найти уровень с максимальным кол-вом элементов");
        setContentPane(contentPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int width = dimension.width / 100 * 90;
        int height = dimension.height / 100 * 90;
        setBounds(
                (dimension.width - width) / 2,
                (dimension.height - height) / 2,
                width,
                height
        );
        paintPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics gr) {
                super.paintComponent(gr);
                Dimension paintSize;
                if (binary) {
                    paintSize = BinaryTreePainter.paint(binaryTree, gr);
                } else {
                    paintSize = SimpleTreePainter.paint(simpleTree, gr);
                }
                if (!paintSize.equals(this.getPreferredSize())) {
                    SwingUtils.setFixedSize(this, paintSize.width, paintSize.height);
                }
            }
        };
        JScrollPane paintJScrollPane = new JScrollPane(paintPanel);
        panelPaintArea.add(paintJScrollPane);

        buttonBuildBrakedBinaryTree.addActionListener(e -> {
            try {
                BinaryTree<Integer> tree = new BinaryTree<>(Integer::parseInt);
                tree.fromBracketNotation(textFieldBrakedNotationBinaryTree.getText());
                this.binaryTree = tree;
                binary = true;
                repaintTree();
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });
        buttonBuildBrakedTree.addActionListener(e -> {
            try {
                SimpleTree<Integer> tree = new SimpleTree<>(Integer::parseInt);
                tree.fromBracketNotation(textFieldBrakedNotationTree.getText());
                this.simpleTree = tree;
                binary = false;
                repaintTree();
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });
        buttonFindMaxElementsLevelBinaryTree.addActionListener(e -> {
            try {
                BinaryTree<Integer> tree = new BinaryTree<>(Integer::parseInt);
                tree.fromBracketNotation(textFieldBrakedNotationBinaryTree.getText());
                this.binaryTree = tree;
                List<Integer> maxLevels = this.binaryTree.findLevelsWithMaxElements();
                this.binaryTree.colorLevelsWithMaxElements(maxLevels);
                repaintTree();
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });
    }
    private void repaintTree() {
        paintPanel.repaint();
    }
}
