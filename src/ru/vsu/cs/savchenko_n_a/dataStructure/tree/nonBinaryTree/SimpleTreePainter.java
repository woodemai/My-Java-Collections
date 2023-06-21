package ru.vsu.cs.savchenko_n_a.dataStructure.tree.nonBinaryTree;

import ru.vsu.cs.savchenko_n_a.utils.DrawUtils;

import java.awt.*;

public class SimpleTreePainter {
    public static final int NODE_WIDTH = 70;
    public static final int NODE_HEIGHT = 30;
    public static final int HORIZONTAL_INDENT = 10;
    public static final int VERTICAL_INDENT = 40;
    public static final Font FONT = new Font("Microsoft Sans Serif", Font.PLAIN, 20);

    private static class NodeDrawResult {
        public int minX;
        public int maxX;
        public int maxY;

        public NodeDrawResult(int minX, int maxX, int maxY) {
            this.minX = minX;
            this.maxX = maxX;
            this.maxY = maxY;
        }
    }

    private static <T extends Comparable<T>> NodeDrawResult paint(ITree.Node<T> node, Graphics2D g2d,
                                                                  int x, int y) {
        if (node == null) {
            return new NodeDrawResult(x, x, y);
        }

        int minX = x;
        int maxX = x;
        int maxY = y;

        int childrenCount = node.getChildren().size();
        int totalWidth = childrenCount * NODE_WIDTH + (childrenCount - 1) * HORIZONTAL_INDENT;

        int startX = x - totalWidth / 2;

        for (ITree.Node<T> child : node.getChildren()) {
            NodeDrawResult childResult = paint(child, g2d, startX, y + NODE_HEIGHT + VERTICAL_INDENT);

            int childX = (childResult.minX + childResult.maxX) / 2;
            g2d.setColor(node.getColor());
            g2d.fillRect(childX - NODE_WIDTH / 2, y, NODE_WIDTH, NODE_HEIGHT);
            g2d.setColor(Color.BLACK);
            g2d.drawLine(childX, y + NODE_HEIGHT, (x + childX) / 2, y + NODE_HEIGHT + VERTICAL_INDENT / 2);
            g2d.drawRect(childX - NODE_WIDTH / 2, y, NODE_WIDTH, NODE_HEIGHT);
            g2d.setColor(DrawUtils.getContrastColor(node.getColor()));
            DrawUtils.drawStringInCenter(g2d, FONT, child.getValue().toString(), childX - NODE_WIDTH / 2, y, NODE_WIDTH, NODE_HEIGHT);

            minX = Math.min(minX, childResult.minX);
            maxX = Math.max(maxX, childResult.maxX);
            maxY = Math.max(maxY, childResult.maxY);

            startX += NODE_WIDTH + HORIZONTAL_INDENT;
        }

        return new NodeDrawResult(minX, maxX, maxY);
    }

    public static <T extends Comparable<T>> Dimension paint(SimpleTree<T> tree, Graphics gr) {
        Graphics2D g2d = (Graphics2D) gr;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        NodeDrawResult rootResult = paint(tree.getRoot(), g2d, HORIZONTAL_INDENT, HORIZONTAL_INDENT);
        return new Dimension(rootResult.maxX + HORIZONTAL_INDENT, rootResult.maxY + HORIZONTAL_INDENT);
    }
}
