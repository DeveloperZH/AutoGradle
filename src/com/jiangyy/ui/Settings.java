package com.jiangyy.ui;

import com.alibaba.fastjson.JSON;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.jiangyy.entity.Repository;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.jiangyy.entity.DataKt.JSON_STR;

public class Settings implements Configurable {

    private JPanel contentPane;
    private JPanel bottomPane;
    private JButton buttonRefresh;
    private JButton buttonReset;
    private JPanel tablePane;
    private JButton 新增Button;

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

        List<Repository> data = JSON.parseArray(JSON_STR, Repository.class);
        List<Repository> data0 = JSON.parseArray(JSON_STR, Repository.class);

        if (data != null && data0 != null) {
            JBTable table = new JBTable(new SettingsTableModel(data));
            table.setRowHeight(36);
            table.setPreferredScrollableViewportSize(new Dimension(800, 300));
            table.setFillsViewportHeight(true);
            JBScrollPane scrollPane = new JBScrollPane(table);
            tablePane.setLayout(new GridLayout(1, 0));
            tablePane.add(scrollPane);
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
        return false;
    }

    /**
     * 点击【apply】、【OK】时，调用
     *
     * @throws ConfigurationException
     */
    @Override
    public void apply() throws ConfigurationException {

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
