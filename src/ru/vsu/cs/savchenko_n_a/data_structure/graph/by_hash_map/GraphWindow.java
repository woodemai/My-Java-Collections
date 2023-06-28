package ru.vsu.cs.savchenko_n_a.data_structure.graph.by_hash_map;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import ru.vsu.cs.savchenko_n_a.data_structure.graph.GraphUtils;
import ru.vsu.cs.savchenko_n_a.utils.IOUtils;
import ru.vsu.cs.savchenko_n_a.utils.JTableUtils;
import ru.vsu.cs.savchenko_n_a.utils.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.NoSuchElementException;


public class GraphWindow extends JFrame {
    private JPanel contentPane;
    private JButton buttonLoadFromFile;
    private JButton buttonSaveToFile;
    private JTable tableGraphData;
    private JButton buttonBuildGraph;
    private final JFileChooser fileChooserTxtOpen = new JFileChooser();
    private final JFileChooser fileChooserTxtSave = new JFileChooser();
    private JPanel panelGraph;
    private JButton buttonBuildMinimumRoads;
    private JButton buttonBuildDigraph;
    private JButton buttonBuildWeightedGraph;
    private JButton buttonBuildWeightedDigraph;
    private JButton buttonFindMinimumRoads;
    private transient Graph graph;

    public GraphWindow() {
        setTitle("Самый короткий цикл");
        setContentPane(contentPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setUndecorated(false);
        JTableUtils.initJTableForArray(tableGraphData, 80, false, true, true, true);

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
                try {
                    String[][] array = IOUtils.getStringArrayFromFile(fileChooserTxtOpen.getSelectedFile().getPath());
                    JTableUtils.writeArrayToJTable(tableGraphData, array);
                } catch (Exception ex) {
                    showReadingErrorMessage(ex);
                }
            }
        });
        buttonSaveToFile.addActionListener(e -> {
            if (fileChooserTxtSave.showSaveDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooserTxtSave.getSelectedFile();
                try {
                    int[][] array = JTableUtils.readIntMatrixFromJTable(tableGraphData);
                    IOUtils.setIntMatrixToFile(file, array);
                    SwingUtils.showInfoMessageBox("Данные успешно сохранены в файл " + file.getName(), "Успех");
                } catch (Exception ex) {
                    showReadingErrorMessage(ex);
                }
            }
        });
        buttonBuildGraph.addActionListener(e -> buildGraph(false, panelGraphvizPainter, false, false));
        buttonBuildDigraph.addActionListener(e -> buildGraph(false, panelGraphvizPainter, true, false));
        buttonBuildWeightedGraph.addActionListener(e -> buildGraph(false, panelGraphvizPainter, false, true));
        buttonBuildWeightedDigraph.addActionListener(e -> buildGraph(false, panelGraphvizPainter, true, true));
        buttonBuildMinimumRoads.addActionListener(e -> buildGraph(true, panelGraphvizPainter, false, true));
        buttonFindMinimumRoads.addActionListener(e -> buildGraphMinimumRoads(panelGraphvizPainter));
        setVisible(true);
    }


    /**
     * @param dotSrc строка в формате dot, описывающая граф
     * @return строку, описывающую svg картинку
     * @throws IOException если введена неверная строка, выбросится исключение
     */
    private static String dotToSvg(String dotSrc) throws IOException {
        MutableGraph g = new Parser().read(dotSrc);
        return Graphviz.fromGraph(g).render(Format.SVG).toString();
    }


    private void showReadingErrorMessage(Exception exception) {
        SwingUtils.showInfoMessageBox("Произошла ошибка при чтение данных " + exception.getMessage() + "\nПопробуйте еще раз", "Ошибка");
        exception.printStackTrace();
    }

    private void getGraph(boolean withMinimumSpanningTree) throws IOException, ParseException {
        int[][] array = JTableUtils.readIntMatrixFromJTable(tableGraphData);
        if (array == null || (array[0].length != 3)) throw new IOException("Неправильно заполнен массив");
        this.graph = new Graph(array, withMinimumSpanningTree);
    }

    private void buildGraph(boolean withMinimumSpanningTree, GraphUtils.SvgPanel svgPanel, boolean directed, boolean weighted) {
        try {
            getGraph(withMinimumSpanningTree);
            String dot;
            if (weighted) {
                if (directed) {
                    dot = graph.toDotWeightedDigraph();
                } else {
                    dot = graph.toDotWeightedGraph();
                }
            } else if (directed) {
                dot = graph.toDotDigraph();
            } else {
                dot = graph.toDotGraph();
            }
            svgPanel.paint(dotToSvg(dot));
        } catch (ParseException | IOException | NoSuchElementException ex) {
            showReadingErrorMessage(ex);
        }
    }

    private void buildGraphMinimumRoads(GraphUtils.SvgPanel svgPanel) {
        try {
            getGraph(false);
            svgPanel.paint(dotToSvg(graph.toDotWithImportantRoads(graph.findMinimumRoads())));
        } catch (IOException | ParseException ex) {
            showReadingErrorMessage(ex);
        }
    }

}
