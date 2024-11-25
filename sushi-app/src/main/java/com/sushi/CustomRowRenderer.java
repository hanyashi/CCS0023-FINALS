package com.sushi;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomRowRenderer extends DefaultTableCellRenderer {
    private final int checkboxColumnIndex;

    public CustomRowRenderer(int checkboxColumnIndex) {
        this.checkboxColumnIndex = checkboxColumnIndex;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        setBorder(BorderFactory.createMatteBorder(1, 1,
                (row == table.getRowCount() - 1 ? 1 : 0), 1, Color.decode("#211A1E")));

        Color alternateColor = Color.decode("#B5CBBC");
        Color defaultColor = Color.decode("#CCDAD1");
        Color checkedColor = Color.decode("#99C567");

        Boolean isChecked = (Boolean) table.getValueAt(row, checkboxColumnIndex);

        if (isChecked != null && isChecked) {
            cell.setBackground(checkedColor);
        } else {
            cell.setBackground(isSelected ? Color.decode("#FF8552") : (row % 2 == 0 ? alternateColor : defaultColor));
            cell.setForeground(isSelected ? Color.decode("#211A1E") : Color.decode("#211A1E"));
        }
        
        if (column == 4 || column == 5 || column == 6 || column == 8 || column == 9) {
            setHorizontalAlignment(SwingConstants.CENTER);
        } 
        // 4 5 6 8 9

        return cell;
    }
}
