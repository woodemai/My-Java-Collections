package ru.vsu.cs.savchenko_n_a.data_structure.graph.by_edj_list;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import ru.vsu.cs.savchenko_n_a.data_structure.graph.GraphUtils;
import ru.vsu.cs.savchenko_n_a.utils.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Window extends JFrame {
    private JTextArea textAreaGraph;
    private JPanel contentPane;
    private JButton buttonLoadFromFile;
    private JButton buttonSaveToFile;
    private JButton buttonBuildGraph;
    private JButton buttonFindShortestCycle;
    private JPanel panelGraph;
    private JTextField textFieldShortestCycle;
    private transient Graph graph = null;
    private final JFileChooser fileChooserTxtOpen = new JFileChooser();
    private final JFileChooser fileChooserTxtSave = new JFileChooser();

    public Window() {
        setTitle("Самый короткий цикл");
        setContentPane(contentPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setSize(dimension);
        setVisible(true);

        fileChooserTxtOpen.setCurrentDirectory(new File("./"));
        fileChooserTxtSave.setCurrentDirectory(new File("./"));
        FileFilter txtFilter = new FileNameExtensionFilter("Text files (*.txt)", "txt");

        fileChooserTxtOpen.addChoosableFileFilter(txtFilter);
        fileChooserTxtSave.addChoosableFileFilter(txtFilter);
        fileChooserTxtSave.setAcceptAllFileFilterUsed(false);
        fileChooserTxtSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserTxtSave.setApproveButtonText("Save");

        panelGraph.setLayout(new BorderLayout());
        GraphUtils.SvgPanel panelGraphvizPainter = new GraphUtils.SvgPanel();
        panelGraph.add(new JScrollPane(panelGraphvizPainter));


        buttonLoadFromFile.addActionListener(e -> {
            if (fileChooserTxtOpen.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
                try (Scanner sc = new Scanner(fileChooserTxtOpen.getSelectedFile())) {
                    sc.useDelimiter("\\Z");
                    textAreaGraph.setText(sc.next());
                } catch (Exception exc) {
                    SwingUtils.showInfoMessageBox("Ошибка при загрузки данных из файла.\nПопробуйте еще раз", "Ошибка");
                }
            }
        });
        buttonSaveToFile.addActionListener(e -> {
            if (fileChooserTxtSave.showSaveDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooserTxtSave.getSelectedFile().getPath();
                if (!filename.toLowerCase().endsWith(".txt")) {
                    filename += ".txt";
                }
                try (FileWriter wr = new FileWriter(filename)) {
                    wr.write(textAreaGraph.getText());
                } catch (Exception exc) {
                    SwingUtils.showInfoMessageBox("Ошибка при сохранении данных в файл.\nПопробуйте еще раз", "Ошибка");
                }
            }
        });
        buttonBuildGraph.addActionListener(e -> {
            try {
                Graph graph = GraphUtils.fromStr(textAreaGraph.getText());
                this.graph = graph;
                panelGraphvizPainter.paint(dotToSvg(GraphUtils.toDot(graph)));
            } catch (Exception exc) {
                SwingUtils.showInfoMessageBox("Вы ввели неверные значения!\nПопробуйте еще раз", "Ошибка");
            }
        });
        buttonFindShortestCycle.addActionListener(e -> {
            try {
                Graph graph = GraphUtils.fromStr(textAreaGraph.getText());
                this.graph = graph;
                List<Integer> shortestCycle = this.graph.findShortestCycle();
                panelGraphvizPainter.paint(dotToSvg(GraphUtils.toDotWithShortestCycle(graph, shortestCycle)));
                textFieldShortestCycle.setText(shortestCycle.toString());
            } catch (Exception exc) {
                SwingUtils.showInfoMessageBox("Вы ввели неверные значения!\nПопробуйте еще раз", "Ошибка");
            }
        });
    }

    private static String dotToSvg(String dotSrc) throws IOException {
        MutableGraph g = new Parser().read(dotSrc);
        return Graphviz.fromGraph(g).render(Format.SVG).toString();
    }
}
