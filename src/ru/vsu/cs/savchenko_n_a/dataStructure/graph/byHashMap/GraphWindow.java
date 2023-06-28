package ru.vsu.cs.savchenko_n_a.dataStructure.graph.byHashMap;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import ru.vsu.cs.savchenko_n_a.dataStructure.graph.GraphUtils;
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
    private Graph graph;

    public GraphWindow() {
        setTitle("Самый короткий цикл");
        setContentPane(contentPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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
                } catch (Exception exc) {
                    SwingUtils.showInfoMessageBox("Ошибка при загрузки данных из файла.\nПопробуйте еще раз", "Ошибка");
                    exc.printStackTrace();
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
                } catch (Exception exc) {
                    SwingUtils.showInfoMessageBox(
                            "Ошибка при сохранении данных " + exc.getMessage() + "\nПопробуйте еще раз",
                            "Ошибка");
                }
            }
        });
        buttonBuildGraph.addActionListener(e -> {
            try {
                int[][] array = JTableUtils.readIntMatrixFromJTable(tableGraphData);
                if (array == null || (array[0].length != 3)) throw new NoSuchElementException("Массив пуст");
                this.graph = new Graph(array);
                panelGraphvizPainter.paint(dotToSvg(graph.toDotGraph()));
            } catch (ParseException ex) {
                SwingUtils.showInfoMessageBox("Ошибка при чтении данных из таблицы");
            } catch (IOException ex) {
                SwingUtils.showInfoMessageBox("Ошибка входных данных");
            } catch (NoSuchElementException ex) {
                SwingUtils.showInfoMessageBox(ex.getMessage() + "\nПопробуйте еще раз");
            }
        });
        buttonBuildDigraph.addActionListener(e -> {
            try {
                int[][] array = JTableUtils.readIntMatrixFromJTable(tableGraphData);
                if (array == null || (array[0].length != 3)) throw new NoSuchElementException("Массив пуст");
                this.graph = new Graph(array);
                panelGraphvizPainter.paint(dotToSvg(graph.toDotDigraph()));
            } catch (ParseException ex) {
                SwingUtils.showInfoMessageBox("Ошибка при чтении данных из таблицы");
            } catch (IOException ex) {
                SwingUtils.showInfoMessageBox("Ошибка входных данных");
            } catch (NoSuchElementException ex) {
                SwingUtils.showInfoMessageBox(ex.getMessage() + "\nПопробуйте еще раз");
            }
        });
        buttonBuildWeightedGraph.addActionListener(e -> {
            try {
                int[][] array = JTableUtils.readIntMatrixFromJTable(tableGraphData);
                if (array == null || (array[0].length != 3)) throw new NoSuchElementException("Массив пуст");
                this.graph = new Graph(array);
                panelGraphvizPainter.paint(dotToSvg(graph.toDotWeightedGraph()));
            } catch (ParseException ex) {
                SwingUtils.showInfoMessageBox("Ошибка при чтении данных из таблицы");
            } catch (IOException ex) {
                SwingUtils.showInfoMessageBox("Ошибка входных данных");
            } catch (NoSuchElementException ex) {
                SwingUtils.showInfoMessageBox(ex.getMessage() + "\nПопробуйте еще раз");
            }
        });
        buttonBuildWeightedDigraph.addActionListener(e -> {
            try {
                int[][] array = JTableUtils.readIntMatrixFromJTable(tableGraphData);
                if (array == null || (array[0].length != 3)) throw new NoSuchElementException("Массив пуст");
                this.graph = new Graph(array);
                panelGraphvizPainter.paint(dotToSvg(graph.toDotWeightedDigraph()));
            } catch (ParseException ex) {
                SwingUtils.showInfoMessageBox("Ошибка при чтении данных из таблицы");
            } catch (IOException ex) {
                SwingUtils.showInfoMessageBox("Ошибка входных данных");
            } catch (NoSuchElementException ex) {
                SwingUtils.showInfoMessageBox(ex.getMessage() + "\nПопробуйте еще раз");
            }
        });
        buttonBuildMinimumRoads.addActionListener(e -> {
            try {
                int[][] array = JTableUtils.readIntMatrixFromJTable(tableGraphData);
                if (array == null || (array[0].length != 3)) throw new NoSuchElementException("Массив пуст");
                this.graph = new Graph(array, true);
                panelGraphvizPainter.paint(dotToSvg(graph.toDotWeightedGraph()));
            } catch (ParseException ex) {
                SwingUtils.showInfoMessageBox("Ошибка при чтении данных из таблицы");
            } catch (IOException ex) {
                SwingUtils.showInfoMessageBox("Ошибка входных данных");
            } catch (NoSuchElementException ex) {
                SwingUtils.showInfoMessageBox(ex.getMessage() + "\nПопробуйте еще раз");
            }
        });
        buttonFindMinimumRoads.addActionListener(e -> {
            try {
                int[][] array = JTableUtils.readIntMatrixFromJTable(tableGraphData);
                if (array == null || (array[0].length != 3)) throw new NoSuchElementException("Массив пуст");
                this.graph = new Graph(array);
                panelGraphvizPainter.paint(dotToSvg(graph.toDotWithImportantRoads(this.graph.findMinimumRoads())));
            } catch (ParseException ex) {
                SwingUtils.showInfoMessageBox("Ошибка при чтении данных из таблицы");
            } catch (IOException ex) {
                SwingUtils.showInfoMessageBox("Ошибка входных данных");
            } catch (NoSuchElementException ex) {
                SwingUtils.showInfoMessageBox(ex.getMessage() + "\nПопробуйте еще раз");
            }
        });
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
}
