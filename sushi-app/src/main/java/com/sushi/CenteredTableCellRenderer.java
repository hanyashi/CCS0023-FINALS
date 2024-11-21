package com.sushi;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

class CenteredTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setBorder(BorderFactory.createMatteBorder(1, 1,
                (row == table.getRowCount() - 1 ? 1 : 0), 1, Color.decode("#211A1E")));

        Color alternateColor = Color.decode("#B5CBBC");
        Color defaultColor = Color.decode("#CCDAD1");
        setBackground(isSelected ? Color.decode("#99C567") : (row % 2 == 0 ? alternateColor : defaultColor));
        setHorizontalAlignment(SwingConstants.CENTER);
        return this;
    }
}
