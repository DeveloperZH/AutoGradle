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
import java.net.URI;
import java.util.List;

import static com.jiangyy.entity.DataKt.JSON_STR;

public class Settings implements Configurable {

    private JPanel contentPane;
    private JPanel bottomPane;
    private JButton buttonRefresh;
    private JButton buttonReset;
    private JPanel tablePane;
    private JButton 新增Button;
    private JButton 删除Button;
    private JLabel label_text;

    private String jsonStr = "";
    private int row = 0;
    private boolean isModified = false;

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

        jsonStr = PropertiesComponent.getInstance().getValue("alldata");
        if (jsonStr == null || jsonStr.isEmpty()) {
            jsonStr = JSON_STR;
            PropertiesComponent.getInstance().setValue("alldata", JSON_STR);
        }
        List<Repository> data = JSON.parseArray(jsonStr, Repository.class);
        List<Repository> data0 = JSON.parseArray(jsonStr, Repository.class);
        删除Button.setEnabled(false);

        if (data != null && data0 != null) {
            SettingsTableModel model = new SettingsTableModel(data);
            JBTable table = new JBTable(model);
            删除Button.addActionListener(ev -> {
                data.remove(row);
                model.notify(data);
                isModified = true;
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
                    删除Button.setEnabled(true);
                    row = table.rowAtPoint(e.getPoint());
                    label_text.setText("已选中：" + data.get(row).getName());
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
        PropertiesComponent.getInstance().setValue("alldata", jsonStr);
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
