package com.jiangyy.ui;

import com.jiangyy.entity.Repository;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SettingsTableModel extends AbstractTableModel {

    private List<Repository> allData;

    public SettingsTableModel(List<Repository> ALL_DATA) {
        this.allData = ALL_DATA;
    }

    private String[] columnNames = {"Repository", "Version", "Version_x", "Address"};

    @Override
    public int getRowCount() {
        return allData == null ? 0 : allData.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object o;
        switch (columnIndex) {
            case 0: {
                o = allData.get(rowIndex).getName();
                break;
            }
            case 1: {
                o = allData.get(rowIndex).getVersion();
                break;
            }
            case 2: {
                o = allData.get(rowIndex).getVersion_x();
                break;
            }
            case 3: {
                o = "Github";
                break;
            }
            case 4: {
                o = allData.get(rowIndex).getRemark();
                break;
            }
            default: {
                o = "";
                break;
            }
        }
        return o;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 2:
                allData.get(rowIndex).setVersion((String) aValue);
                break;
            case 3:
                allData.get(rowIndex).setVersion_x((String) aValue);
                break;
            case 5:
                allData.get(rowIndex).setRemark((String) aValue);
                break;
            default:
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }



}