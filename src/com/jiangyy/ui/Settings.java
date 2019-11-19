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
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

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

        tempRepositoriesStr = PropertiesComponent.getInstance().getValue("alldata");
        if (tempRepositoriesStr == null || tempRepositoriesStr.isEmpty()) {
            tempRepositoriesStr = JSON_STR;
            PropertiesComponent.getInstance().setValue("alldata", JSON_STR);
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
            dialog.setOnResultListener(new AddDialog.OnResultListener() {
                @Override
                public void onResult(Repository repository) {
                    tempRepositories.set(row, repository);
                    model.notify(tempRepositories);
                    isModified = true;
                    labelCheck.setText("");
                }
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
        buttonReset.addActionListener(e->{
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
                model.notify(tempRepositories);
                isModified = true;
                labelCheck.setText("");
            });
            table.setRowHeight(36);
            table.setPreferredScrollableViewportSize(new Dimension(800, 300));
            table.setFillsViewportHeight(true);
            JBScrollPane scrollPane = new JBScrollPane(table);
            tablePane.setLayout(new GridLayout(1, 0));
            tablePane.add(scrollPane);
            table.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

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
        PropertiesComponent.getInstance().setValue("alldata", tempRepositoriesStr);
    }

    /**
     * 点击【Reset】时，调用
     */
    @Override
    public void reset() {

    }

    @Override
    public void disposeUIResources() {

    }
}
