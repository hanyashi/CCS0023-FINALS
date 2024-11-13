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
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        setBorder(BorderFactory.createMatteBorder(1, 1,
                (row == table.getRowCount() - 1 ? 1 : 0), 1, Color.decode("#211A1E")));

        Color alternateColor = Color.decode("#B5CBBC");
        Color defaultColor = Color.decode("#CCDAD1");

        if (!isSelected) {
            setBackground(row % 2 == 0 ? alternateColor : defaultColor);
        } else {
            setBackground(Color.decode("#99C567"));
            setForeground(Color.decode("#211A1E"));
        }

        var checkbox = new JCheckBox();
        checkbox.setBorder(BorderFactory.createEmptyBorder());

        this.add(checkbox, BorderLayout.CENTER);

        return this;
    }
}