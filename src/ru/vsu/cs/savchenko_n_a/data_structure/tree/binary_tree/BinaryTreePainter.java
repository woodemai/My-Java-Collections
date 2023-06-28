package ru.vsu.cs.savchenko_n_a.data_structure.tree.binary_tree;

import ru.vsu.cs.savchenko_n_a.utils.DrawUtils;

import java.awt.*;

public class BinaryTreePainter {
    public static final int NODE_WIDTH = 70;
    public static final int NODE_HEIGHT = 30;
    public static final int HORIZONTAL_INDENT = 10;
    public static final int VERTICAL_INDENT = 50;
    public static final Font FONT = new Font("Microsoft Sans Serif", Font.PLAIN, 20);

    private static class NodeDrawResult {

        public int center;
        public int maxX;
        public int maxY;

        public NodeDrawResult(int center, int maxX, int maxY) {
            this.center = center;
            this.maxX = maxX;
            this.maxY = maxY;
        }
    }

    private static <T extends Comparable<T>> NodeDrawResult paint(IBinaryTree.Node<T> node, Graphics2D g2d,
                                                                  int x, int y) {
        if (node == null) {
            return null;
        }

        NodeDrawResult leftResult = paint(node.getLeft(), g2d, x, y + (NODE_HEIGHT + VERTICAL_INDENT));
        int rightX = (leftResult != null) ? leftResult.maxX : x + (NODE_WIDTH + HORIZONTAL_INDENT) / 2;
        NodeDrawResult rightResult = paint(node.getRight(), g2d, rightX, y + (NODE_HEIGHT + VERTICAL_INDENT));
        int thisX;
        if (leftResult == null) {
            thisX = x;
        } else if (rightResult == null) {
            thisX = Math.max(x + (NODE_WIDTH + HORIZONTAL_INDENT) / 2, leftResult.center + HORIZONTAL_INDENT / 2);
        } else {
            thisX = (leftResult.center + rightResult.center) / 2 - NODE_WIDTH / 2;
        }

        Color color = node.getColor();
        g2d.setColor(color);
        g2d.fillRect(thisX, y, NODE_WIDTH, NODE_HEIGHT);
        g2d.setColor(Color.BLACK);
        if (leftResult != null) {
            g2d.drawLine(thisX + NODE_WIDTH / 2, y + NODE_HEIGHT, leftResult.center, y + NODE_HEIGHT + VERTICAL_INDENT);
        }
        if (rightResult != null) {
            g2d.drawLine(thisX + NODE_WIDTH / 2, y + NODE_HEIGHT, rightResult.center, y + NODE_HEIGHT + VERTICAL_INDENT);
        }
        g2d.drawRect(thisX, y, NODE_WIDTH, NODE_HEIGHT);
        g2d.setColor(DrawUtils.getContrastColor(color));
        DrawUtils.drawStringInCenter(g2d, FONT, node.getValue().toString(), thisX, y, NODE_WIDTH, NODE_HEIGHT);

        int maxX = Math.max((leftResult == null) ? 0 : leftResult.maxX, (rightResult == null) ? 0 : rightResult.maxX);
        int maxY = Math.max((leftResult == null) ? 0 : leftResult.maxY, (rightResult == null) ? 0 : rightResult.maxY);
        return new NodeDrawResult(
                thisX + NODE_WIDTH / 2,
                Math.max(thisX + NODE_WIDTH + HORIZONTAL_INDENT, maxX),
                Math.max(y + NODE_HEIGHT, maxY)
        );
    }

    public static <T extends Comparable<T>> Dimension paint(BinaryTree<T> tree, Graphics gr) {
        Graphics2D g2d = (Graphics2D) gr;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        NodeDrawResult rootResult = paint(tree.getRoot(), g2d, HORIZONTAL_INDENT, HORIZONTAL_INDENT);
        return new Dimension((rootResult == null) ? 0 : rootResult.maxX, (rootResult == null) ? 0 : rootResult.maxY + HORIZONTAL_INDENT);
    }
}
