package com.jiangyy.ui;

import com.alibaba.fastjson.JSON;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.jiangyy.entity.Repository;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.util.List;

import static com.jiangyy.constant.Constant.KEY;
import static com.jiangyy.entity.DataKt.JSON_STR;

public class Settings implements Configurable {

    private JPanel contentPane;
    private JPanel bottomPane;
    private JButton buttonReset;
    private JPanel tablePane;
    private JButton buttonAdd;
    private JButton buttonDelete;
    private JLabel labelCheck;
    private JButton buttonEdit;

    private int row = 0;
    private boolean isModified = false;

    private SettingsTableModel model;

    private List<Repository> ORepositories; // 原始数据，永不会变

    private List<Repository> tempRepositories;
    private String tempRepositoriesStr;

    /**
     * 在settings中显示的名称
     *
     * @return
     */
    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "AutoGradle";
    }

    /**
     * 初始化控件
     *
     * @return
     */
    @Nullable
    @Override
    public JComponent createComponent() {

        tempRepositoriesStr = PropertiesComponent.getInstance().getValue(KEY);
        if (tempRepositoriesStr == null || tempRepositoriesStr.isEmpty()) {
            tempRepositoriesStr = JSON_STR;
            PropertiesComponent.getInstance().setValue(KEY, JSON_STR);
        }
        ORepositories = JSON.parseArray(JSON_STR, Repository.class);
        tempRepositories = JSON.parseArray(tempRepositoriesStr, Repository.class);
        buttonDelete.setEnabled(false);
        buttonEdit.setEnabled(false);
        buttonEdit.addActionListener(e -> {
            AddDialog dialog = new AddDialog(tempRepositories.get(row));
            int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
            int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
            dialog.setLocation((screenWidth - dialog.getSize().width) / 2, (screenHeight - dialog.getSize().height) / 2);
            dialog.setOnResultListener(repository -> {
                tempRepositories.set(row, repository);
                model.notify(tempRepositories);
                isModified = true;
                labelCheck.setText("");
            });
            dialog.pack();
            dialog.setVisible(true);
        });
        buttonAdd.addActionListener(ev -> {
            AddDialog dialog = new AddDialog();
            int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
            int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
            dialog.setBounds(
                    0, 0, 800, 400
            );
//            dialog.setLocation((screenWidth - dialog.getSize().width) / 2, (screenHeight - dialog.getSize().height) / 2);
            dialog.setOnResultListener(new AddDialog.OnResultListener() {
                @Override
                public void onResult(Repository repository) {
                    tempRepositories.add(repository);
                    model.notify(tempRepositories);
                    isModified = true;
//                    labelCheck.setText("");
                }
            });
            dialog.pack();
            dialog.setVisible(true);
        });
        buttonReset.addActionListener(e -> {
            tempRepositories = ORepositories;
            model.notify(tempRepositories);
            isModified = true;
            labelCheck.setText("");
        });
        if (tempRepositories != null) {
            model = new SettingsTableModel(tempRepositories);
            JBTable table = new JBTable(model);
            buttonDelete.addActionListener(ev -> {
                tempRepositories.remove(row);
                buttonDelete.setEnabled(false);
                buttonEdit.setEnabled(false);
                model.notify(tempRepositories);
                isModified = true;
                labelCheck.setText("");
            });
            table.setRowHeight(36);
            table.setPreferredScrollableViewportSize(new Dimension(800, 300));
            table.setFillsViewportHeight(true);
            TableColumn c = table.getColumnModel().getColumn(3);
            c.setMinWidth(80);
            JBScrollPane scrollPane = new JBScrollPane(table);
            tablePane.setLayout(new GridLayout(1, 0));
            tablePane.add(scrollPane);
            table.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    if (col == 3) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            URI uri = new URI("http://www.github.com/" + tempRepositories.get(row).getUser() + "/" + tempRepositories.get(row).getName());
                            desktop.browse(uri);
                        } catch (Exception ex) {
                            // do nothing
                            ex.printStackTrace();
                        }

                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    buttonDelete.setEnabled(true);
                    buttonEdit.setEnabled(true);
                    row = table.rowAtPoint(e.getPoint());
                    labelCheck.setText("已选中：" + tempRepositories.get(row).getName());
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }
        return contentPane;
    }

    /**
     * 是否修改
     *
     * @return
     */
    @Override
    public boolean isModified() {
        return isModified;
    }

    /**
     * 点击【apply】、【OK】时，调用
     *
     * @throws ConfigurationException
     */
    @Override
    public void apply() throws ConfigurationException {
        tempRepositoriesStr = JSON.toJSONString(tempRepositories);
        PropertiesComponent.getInstance().setValue(KEY, tempRepositoriesStr);
        isModified = false;
    }

    /**
     * 点击【Reset】时，调用
     */
    @Override
    public void reset() {
        isModified = true;
    }

    @Override
    public void disposeUIResources() {

    }
}
