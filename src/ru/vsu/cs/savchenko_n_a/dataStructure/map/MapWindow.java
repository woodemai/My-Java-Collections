package ru.vsu.cs.savchenko_n_a.dataStructure.map;

import ru.vsu.cs.savchenko_n_a.utils.IOUtils;
import ru.vsu.cs.savchenko_n_a.utils.JTableUtils;
import ru.vsu.cs.savchenko_n_a.utils.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class MapWindow extends JFrame {
    private JButton buttonLoadArrayFromFile;
    private JButton buttonSortArrayTreeMap;
    private JTable tableLoadedArray;
    private JTable tableSortedArray;
    private JButton buttonSaveArrayToFile;
    private JPanel contentPane;
    private JButton buttonSortArrayMyTreeMap;
    private final JFileChooser fileChooserTxtOpen = new JFileChooser();
    private final JFileChooser fileChooserTxtSave = new JFileChooser();

    public MapWindow() {
        setTitle("Отсортировать массив с помощью TreeMap");
        setContentPane(contentPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int width = dimension.width / 2;
        int height = dimension.height / 2;
        setBounds(width / 2, height / 2, width, height);
        JTableUtils.initJTableForArray(tableLoadedArray, 80, false, true, false, true);
        JTableUtils.initJTableForArray(tableSortedArray, 80, false, true, false, true);
        setVisible(true);

        fileChooserTxtOpen.setCurrentDirectory(new File("./"));
        fileChooserTxtSave.setCurrentDirectory(new File("./"));
        FileFilter txtFilter = new FileNameExtensionFilter("Text files (*.txt)", "txt");

        fileChooserTxtOpen.addChoosableFileFilter(txtFilter);
        fileChooserTxtSave.addChoosableFileFilter(txtFilter);
        fileChooserTxtSave.setAcceptAllFileFilterUsed(false);
        fileChooserTxtSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserTxtSave.setApproveButtonText("Save");

        buttonLoadArrayFromFile.addActionListener(e -> {
            if (fileChooserTxtOpen.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
                try {
                    String[] array = IOUtils.getStringArrayFromFile(fileChooserTxtOpen.getSelectedFile().getPath())[0];
                    JTableUtils.writeArrayToJTable(tableLoadedArray, array);
                } catch (Exception exc) {
                    SwingUtils.showInfoMessageBox("Ошибка при загрузки данных из файла.\nПопробуйте еще раз", "Ошибка");
                    exc.printStackTrace();
                }
            }
        });
        buttonSaveArrayToFile.addActionListener(e -> {
            if (fileChooserTxtSave.showSaveDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooserTxtSave.getSelectedFile();
                try {
                    int[] array = Objects.requireNonNull(JTableUtils.readIntMatrixFromJTable(tableSortedArray))[0];
                    IOUtils.setIntArrayToFile(file, array);
                    SwingUtils.showInfoMessageBox("Данные успешно сохранены в файл " + file.getName(), "Успех");
                } catch (Exception exc) {
                    SwingUtils.showInfoMessageBox(
                            "Ошибка при сохранении данных " + exc.getMessage() + "\nПопробуйте еще раз",
                            "Ошибка");
                }
            }
        });
        buttonSortArrayTreeMap.addActionListener(e -> {
            try {
                int [] arr = JTableUtils.readIntArrayFromJTable(tableLoadedArray);
                JTableUtils.writeArrayToJTable(tableSortedArray, Program.sortIntArray(arr));
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
        buttonSortArrayMyTreeMap.addActionListener(e -> {
            try {
                int [] arr = JTableUtils.readIntArrayFromJTable(tableLoadedArray);
                JTableUtils.writeArrayToJTable(tableSortedArray, Program.myMapSortIntArray(arr));
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
    }
}
