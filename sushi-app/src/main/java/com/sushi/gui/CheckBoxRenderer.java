package com.sushi.gui;

import javax.swing.table.TableCellRenderer;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Color;

public class CheckBoxRenderer extends JPanel implements TableCellRenderer {

    private final JCheckBox checkBox;

    public CheckBoxRenderer() {
        setLayout(new BorderLayout());
        checkBox = new JCheckBox();
        checkBox.setOpaque(false);
        add(checkBox, BorderLayout.CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        if (value instanceof Boolean) {
            checkBox.setSelected((Boolean) value);
        }

        setBorder(BorderFactory.createMatteBorder(1, 1,
                (row == table.getRowCount() - 1 ? 1 : 0), 1, Color.decode("#211A1E")));

        Color alternateColor = Color.decode("#B5CBBC");
        Color defaultColor = Color.decode("#CCDAD1");
        setBackground(isSelected ? Color.decode("#99C567") : (row % 2 == 0 ? alternateColor : defaultColor));

        return this;
    }
}