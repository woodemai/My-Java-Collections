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
    private JButton buttonGet;
    private JTextArea textAreaText;
    private JTable tableArray;
    private JButton buttonSaveArrayToFile;
    private JPanel contentPane;
    private JButton buttonGetMyMap;
    private JTextField textFieldNum;
    private JButton buttonLoadArray;
    private JTable tableLoadedArray;
    private JButton buttonSortArray;
    private JButton buttonSortArrayMy;
    private JTable tableSortedArray;
    private JButton buttonSaveArray;
    private final JFileChooser fileChooserTxtOpen = new JFileChooser();
    private final JFileChooser fileChooserTxtSave = new JFileChooser();

    public MapWindow() {
        setTitle("Найти все слова длины N");
        setContentPane(contentPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int width = dimension.width / 3 * 2;
        int height = dimension.height / 3 * 2;
        setBounds(width / 3, height / 3, width, height);
        JTableUtils.initJTableForArray(tableLoadedArray, 80, false, true, false, true);
        JTableUtils.initJTableForArray(tableSortedArray, 80, false, true, false, true);
        JTableUtils.initJTableForArray(tableArray, 80, false, true, false, true);

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
                    String str = IOUtils.getStringFromFile(fileChooserTxtOpen.getSelectedFile().getPath());
                    textAreaText.setText(str);
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
                    int[] array = Objects.requireNonNull(JTableUtils.readIntMatrixFromJTable(tableArray))[0];
                    IOUtils.setIntArrayToFile(file, array);
                    SwingUtils.showInfoMessageBox("Данные успешно сохранены в файл " + file.getName(), "Успех");
                } catch (Exception exc) {
                    SwingUtils.showInfoMessageBox(
                            "Ошибка при сохранении данных " + exc.getMessage() + "\nПопробуйте еще раз",
                            "Ошибка");
                }
            }
        });
        buttonGet.addActionListener(e -> {
            try {
                String str = textAreaText.getText();
                int num = Integer.parseInt(textFieldNum.getText());
                String[] strings = WordsNTimesMethods.getWords(str, num).toArray(new String[0]);
                JTableUtils.writeArrayToJTable(tableArray, strings);

            } catch (Exception exc) {
                SwingUtils.showInfoMessageBox("Вы ввели неверные значения");
            }
        });
        buttonGetMyMap.addActionListener(e -> {
            try {
                String str = textAreaText.getText();
                int num = Integer.parseInt(textFieldNum.getText());
                String[] strings = WordsNTimesMethods.simpleGetWords(str, num).toArray(new String[0]);
                JTableUtils.writeArrayToJTable(tableArray, strings);
            } catch (Exception exc) {
                SwingUtils.showInfoMessageBox("Вы ввели неверные значения");
            }
        });
        buttonLoadArray.addActionListener(e -> {
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
        buttonSaveArray.addActionListener(e -> {
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
        buttonSortArray.addActionListener(e -> {
            try {
                int[] arr = JTableUtils.readIntArrayFromJTable(tableLoadedArray);
                JTableUtils.writeArrayToJTable(tableSortedArray, SortArrayMethods.sortIntArray(arr));
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
        buttonSortArrayMy.addActionListener(e -> {
            try {
                int[] arr = JTableUtils.readIntArrayFromJTable(tableLoadedArray);
                JTableUtils.writeArrayToJTable(tableSortedArray, SortArrayMethods.myMapSortIntArray(arr));
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
    }
}
