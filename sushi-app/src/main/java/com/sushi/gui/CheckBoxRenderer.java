package com.sushi.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CheckBoxRenderer extends JPanel implements TableCellRenderer {

    private final JCheckBox checkBox;

    public CheckBoxRenderer() {
        setLayout(new BorderLayout());
        checkBox = new JCheckBox();
        checkBox.setOpaque(false);
        add(checkBox, BorderLayout.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        if (value instanceof Boolean) {
            checkBox.setSelected((Boolean) value);
        }

        setBorder(BorderFactory.createMatteBorder(1, 1,
                (row == table.getRowCount() - 1 ? 1 : 0), 1, Color.decode("#211A1E")));

        Color alternateColor = Color.decode("#B5CBBC");
        Color defaultColor = Color.decode("#CCDAD1");
        Color checkedColor = Color.decode("#99C567");

        Boolean isChecked = (Boolean) table.getValueAt(row, 1);

        if (isChecked != null && isChecked) {
            setBackground(checkedColor);
        } else {
            setBackground(isSelected ? Color.decode("#CCDAD1") : (row % 2 == 0 ? alternateColor : defaultColor));
        }

        return this;
    }
}