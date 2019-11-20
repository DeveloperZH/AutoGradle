package com.jiangyy.ui;

import com.jiangyy.entity.Repository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox1;
    private JTextField textFieldUser;
    private JTextField textFieldVersion;
    private JTextField textFieldGradle;
    private JTextField textFieldGradleX;
    private JTextField textFieldVersionX;
    private JTextField textFieldName;
    private JTextField textFieldProcessor;
    private JTextField textFieldProcessorX;

    private OnResultListener onResultListener;

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    interface OnResultListener {
        void onResult(Repository repository);
    }

    public AddDialog(Repository repository) {
        init();
        textFieldUser.setText(repository.getUser());
        textFieldName.setText(repository.getName());
        textFieldGradle.setText(repository.getGradle());
        textFieldGradleX.setText(repository.getGradle_x());
        textFieldProcessor.setText(repository.getProcessor());
        textFieldProcessorX.setText(repository.getProcessor_x());
        textFieldVersion.setText(repository.getVersion());
        textFieldVersionX.setText(repository.getVersion_x());
        repository.getMaven();
        if(!repository.getMaven().isEmpty()){
            comboBox1.setSelectedItem(repository.getMaven());
        }
    }

    public AddDialog() {
        init();
    }

    private void init() {
        setTitle("Add Repository");
        setModal(true);
        setFocusable(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
        // call onCancel() when cross is clicked
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        comboBox1.addItem("JCenter");
        comboBox1.addItem("MavenCenter");
        comboBox1.addItem("JitPack");

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
    }

    private void onOK() {
        Repository repository = new Repository(
                false,
                textFieldName.getText(),
                textFieldUser.getText(),
                textFieldVersion.getText(),
                textFieldVersionX.getText(),
                textFieldGradle.getText(),
                textFieldGradleX.getText(),
                textFieldProcessor.getText(),
                textFieldProcessorX.getText(),
                comboBox1.getSelectedItem().toString(),
                "",
                ""
        );
        if (onResultListener != null) {
            onResultListener.onResult(repository);
            dispose();
        }
    }

    private void onCancel() {
        dispose();
    }

}
