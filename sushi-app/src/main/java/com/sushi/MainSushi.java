package com.sushi;

// imports
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;
import com.sushi.gui.CheckBoxEditor;
import com.sushi.gui.CheckBoxRenderer;

// main class
public class MainSushi {

    // GUI components (swing)
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private final TaskManager manager;
    private final JFrame mainFrame;

    // constructor
    public MainSushi() {
        Font defaultFont = new Font("Montserrat", Font.BOLD, 12);
        manager = new TaskManager();
        mainFrame = new JFrame("Sushi Beta 1.0");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);
        mainFrame.getContentPane().setBackground(Color.decode("#CCDAD1"));
        mainFrame.setResizable(false);

        ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource("assets/sushi-logo.png"));
        mainFrame.setIconImage(logo.getImage());
        swingGUI();
        mainFrame.setVisible(true);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // main GUI setup
    private void swingGUI() {
        JPanel mainPanel = createMainPanel();
        setupTable();

        mainPanel.setBorder(new EmptyBorder(0, 12, 12, 12)); // padding

        mainPanel.add(createTableScrollPane(), BorderLayout.CENTER);
        setupTableBorders();

        mainFrame.add(mainPanel, BorderLayout.CENTER);

        mainFrame.add(createContainerPanel(), BorderLayout.NORTH);
        mainFrame.revalidate();
        mainFrame.repaint();
        refreshTaskTable();

    }

    // table setup methods
    private void setupTable() {
        tableModel = new DefaultTableModel(
                new String[] { "UUID", " ", "Title", "Description", "Due Date", "Priority", "Prev. Status", "Status",
                        "Category" },
                0) {

            // class definitions for the table model
            @Override
            public Class<?> getColumnClass(int column) { // define classes for each column
                switch (column) {
                    case 1:
                        return Boolean.class;

                    default:
                        return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };

        taskTable = new JTable(tableModel);

        var completedColumn = taskTable.getColumn(" ");
        completedColumn.setMaxWidth(24);
        completedColumn.setCellRenderer(new CheckBoxRenderer());
        completedColumn.setCellEditor(new CheckBoxEditor());

        // Makes UUID Column invisible
        var uuidColumn = taskTable.getColumn("UUID");
        uuidColumn.setMinWidth(0);
        uuidColumn.setMaxWidth(0);

        // Makes Prev. Status Column invisible
        // Prev. Status to store prev stat :))
        var prevStatusColumn = taskTable.getColumn("Prev. Status");
        prevStatusColumn.setMinWidth(0);
        prevStatusColumn.setMaxWidth(0);

        taskTable.putClientProperty("terminateEditOnFocusLost", true);
        taskTable.setRowHeight(24);
        taskTable.setFont(new Font("Montserrat", Font.PLAIN, 12));
        taskTable.setIntercellSpacing(new Dimension(0, 0));
        taskTable.getTableHeader().setBackground(Color.decode("#211A1E"));
        taskTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        taskTable.setBackground(Color.decode("#211A1E"));
        taskTable.setFillsViewportHeight(true);
        taskTable.setComponentPopupMenu(createTablePopupMenu());

        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Event listener
        taskTable.getModel().addTableModelListener((e) -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            if (column == 1) {
                Boolean isCompleted = (Boolean) taskTable.getValueAt(row, 1);
                String taskTitle = (String) tableModel.getValueAt(row, 0);
                Task task = manager.getTaskById(taskTitle);
                String status = (String) tableModel.getValueAt(row, 6);

                // Strikethrough title
                Font defaultFont = new Font("Montserrat", Font.PLAIN, 12);
                AttributedString as = new AttributedString(taskTitle);
                as.addAttribute(TextAttribute.FONT, defaultFont);
                if (isCompleted) {
                    as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, 0, taskTitle.length());
                }

                if (task != null) {

                    task.setStatus(isCompleted ? "Complete" : status);
                    task.setCompleted(isCompleted);
                    manager.saveTasks();
                    refreshTaskTable();

                }
            }

        });
    }

    private JScrollPane createTableScrollPane() {
        JScrollPane tableScrollPane = new JScrollPane(taskTable);
        tableScrollPane.setPreferredSize(new Dimension(900, 400));
        tableScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        return tableScrollPane;
    }

    private void setupTableBorders() {
        taskTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Montserrat", Font.BOLD, 16));
                label.setForeground(Color.decode("#CCDAD1"));
                label.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.decode("#211A1E")));
                return label;
            }
        });

        taskTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JComponent) comp).setBorder(BorderFactory.createMatteBorder(1, 1,
                        (row == table.getRowCount() - 1 ? 1 : 0), 1, Color.decode("#211A1E")));

                Color alternateColor = Color.decode("#B5CBBC");
                Color defaultColor = Color.decode("#CCDAD1");

                if (!isSelected) {
                    comp.setBackground(row % 2 == 0 ? alternateColor : defaultColor);
                } else {
                    comp.setBackground(Color.decode("#99C567"));
                    comp.setForeground(Color.decode("#211A1E"));
                }

                return comp;
            }
        });

    }

    private JPanel createContainerPanel() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 144));
        containerPanel.add(createTitlePanel(), BorderLayout.NORTH);
        containerPanel.add(createButtonPanel());

        return containerPanel;
    }

    // title panel creation
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.decode("#211A1E"));
        titlePanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 75));

        JLabel title = new JLabel("Sushi Beta 1.0 ", SwingConstants.LEFT);
        ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource("assets/sushi-logo2.png"));
        title.setIcon(logo);
        title.setIconTextGap(10);
        title.setFont(new Font("Montserrat", Font.BOLD | Font.ITALIC, 24));
        title.setForeground(Color.decode("#FF8552"));
        title.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

        ImageIcon userIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/itamae.png"));
        JButton profileButton = new JButton(userIcon);
        profileButton.addActionListener(e -> addTaskGUI());
        profileButton.setBackground(Color.decode("#211A1E"));
        profileButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        profileButton.setFocusPainted(false);
        profileButton.setRolloverEnabled(false);
        profileButton.setFocusable(false);
        profileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profileButton.setUI(new MetalButtonUI() {
        @Override
        protected void paintButtonPressed(Graphics g, AbstractButton b) {
        }
    });

        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(profileButton, BorderLayout.EAST);
        return titlePanel;
    }

    private JPanel createButtonPanel() {
        ImageIcon addIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/temaki.png"));
        ImageIcon filterIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/sushi-filter.png"));
        ImageIcon sortIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/onigiri.png"));
        

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.decode("#CCDAD1"));
        buttonPanel.setBorder(new EmptyBorder(24, 36, 12, 36));

        JButton addButton = new JButton("Add Task", addIcon);
        addButton.addActionListener(e -> addTaskGUI());
        addButton.setFont(new Font("Montserrat", Font.BOLD, 14));
        addButton.setPreferredSize(new Dimension(132, 40));
        addButton.setBackground(Color.decode("#211A1E"));
        addButton.setForeground(Color.decode("#CCDAD1"));
        addButton.setFocusable(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setIconTextGap(10);

        JButton filterButton = new JButton("Filter", filterIcon);
        filterButton.addActionListener(e -> addTaskGUI());
        filterButton.setFont(new Font("Montserrat", Font.BOLD, 14));
        filterButton.setPreferredSize(new Dimension(105, 40));
        filterButton.setBackground(Color.decode("#211A1E"));
        filterButton.setForeground(Color.decode("#CCDAD1"));
        filterButton.setFocusable(false);
        filterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterButton.setIconTextGap(10);

        JButton sortButton = new JButton("Sort", sortIcon);  
        sortButton.setPreferredSize(new Dimension(95, 40));
        sortButton.setFont(new Font("Montserrat", Font.BOLD, 14));
        sortButton.setBackground(Color.decode("#211A1E"));
        sortButton.setForeground(Color.decode("#CCDAD1"));
        sortButton.setFocusable(false);
        sortButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sortButton.setIconTextGap(10);

        JPopupMenu dropdownMenu = new JPopupMenu();
        dropdownMenu.setBackground(Color.decode("#211A1E"));
    
        String[] options = { "Priority", "Name", "Due Date", "Status", "Category" };
        ButtonGroup buttonGroup = new ButtonGroup();
        Map<String, JCheckBoxMenuItem> menuItems = new HashMap<>();

        for (String option : options) {
            JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(option);
            menuItem.setFont(new Font("Montserrat", Font.PLAIN, 12));
            menuItem.setBackground(Color.decode("#211A1E"));
            menuItem.setForeground(Color.decode("#CCDAD1"));
            menuItem.addActionListener(e -> {
                refreshTaskTable(option);
                menuItems.values().forEach(item -> item.setSelected(false));
                menuItem.setSelected(true);
            });

        menuItems.put(option, menuItem);
        buttonGroup.add(menuItem);
        dropdownMenu.add(menuItem);
    }

    sortButton.addActionListener(e -> dropdownMenu.show(sortButton, 0, sortButton.getHeight()));

        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // 20px horizontal gap (it's invisible, tried using flowlayout hgap but it also added a gap before the add button)
        buttonPanel.add(filterButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(540, 0)));
        buttonPanel.add(sortButton, BorderLayout.EAST);
        return buttonPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.decode("#CCDAD1"));
        return mainPanel;
    }

    // pop-up menu for right-clicking tasks
    private JPopupMenu createTablePopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        Font defaultFont = new Font("Montserrat", Font.PLAIN, 12);
        UIManager.put("editItem.font", defaultFont);

        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(e -> editSelectedTask());

        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(e -> deleteSelectedTask());

        editItem.setFont(defaultFont);
        deleteItem.setFont(defaultFont);
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        return popupMenu;
    }

    // TASK MANAGEMENT METHODS
    // add task method
    private void addTaskGUI() {
        JTextField titleField = new JTextField(10);
        JTextField descriptionField = new JTextField(10);
        JComboBox<String> priorityBox = new JComboBox<>(new String[] { "High", "Medium", "Low" });
        JComboBox<String> statusBox = new JComboBox<>(new String[] { "Pending", "Complete", "Overdue" });
        JSpinner dueDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dueDateSpinner, "MM-dd hh:mm a");
        dueDateSpinner.setEditor(dateEditor);
        JTextField categoryField = new JTextField(10);

        int result = JOptionPane.showConfirmDialog(mainFrame, new Object[] {
                "Title:", titleField,
                "Description:", descriptionField,
                "Priority:", priorityBox,
                "Status:", statusBox,
                "Due Date:", dueDateSpinner,
                "Category:", categoryField
        }, "Add Task", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String priority = (String) priorityBox.getSelectedItem();
            String status = (String) statusBox.getSelectedItem();
            Date dueDate = (Date) dueDateSpinner.getValue();
            String category = categoryField.getText();

            Task task = new Task(false, title, description, dueDate, priority, status, category);
            manager.addTask(task);
            manager.saveTasks();
            refreshTaskTable();
            JOptionPane.showMessageDialog(mainFrame, "Task added successfully.");
        }
        System.out.println("Hi! You are in the addtask method.");
    }

    // delete task method
    private void deleteSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();

        if (selectedRow != -1) {
            String title = (String) taskTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to delete this task?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    manager.removeTask(title);
                    manager.saveTasks();
                    refreshTaskTable();
                    JOptionPane.showMessageDialog(mainFrame, "Task deleted successfully.", "Delete Task",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (TaskNFE e) {
                    JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else {
            JOptionPane.showMessageDialog(mainFrame, "Please select a task to delete.", "No Task Selected",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // edit task method
    private void editSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            String title = (String) taskTable.getValueAt(selectedRow, 0);
            Task task = manager.getTaskById(title);

            if (task != null) {
                JTextField titleField = new JTextField(task.getTitle(), 10);
                JTextField descriptionField = new JTextField(task.getDescription(), 10);
                JComboBox<String> priorityBox = new JComboBox<>(new String[] { "High", "Medium", "Low" });
                priorityBox.setSelectedItem(task.getPriority());
                JComboBox<String> statusBox = new JComboBox<>(new String[] { "Pending", "Complete", "Overdue" });
                statusBox.setSelectedItem(task.getStatus());
                JSpinner dueDateSpinner = new JSpinner(
                        new SpinnerDateModel(task.getDueDate(), null, null, java.util.Calendar.DAY_OF_MONTH));
                JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dueDateSpinner, "MM-dd hh:mm a");
                dueDateSpinner.setEditor(dateEditor);
                JTextField categoryField = new JTextField(task.getCategory(), 10);

                int result = JOptionPane.showConfirmDialog(mainFrame, new Object[] {
                        "Title:", titleField,
                        "Description:", descriptionField,
                        "Priority:", priorityBox,
                        "Status:", statusBox,
                        "Due Date:", dueDateSpinner,
                        "Category:", categoryField
                }, "Edit Task", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);

                if (result == JOptionPane.OK_OPTION) {
                    task.setTitle(titleField.getText());
                    task.setDescription(descriptionField.getText());
                    task.setPriority((String) priorityBox.getSelectedItem());
                    task.setPreviousStatus(task.getStatus());
                    task.setStatus((String) statusBox.getSelectedItem());
                    task.setDueDate((Date) dueDateSpinner.getValue());
                    task.setCategory(categoryField.getText());
                    manager.saveTasks();
                    refreshTaskTable();
                    JOptionPane.showMessageDialog(mainFrame, "Task updated successfully.", "Edit",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Please select a task to edit.", "No Task Selected",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // overloaded method to call without string attrib
    private void refreshTaskTable() {
        refreshTaskTable("Priority");
    }

    // refreshes every time a task has been edited, added, OR deleted
    private void refreshTaskTable(String sortBy) {
        tableModel.setRowCount(0);
        List<Task> tasks = manager.getAllTasks();

        Comparator<Task> comparator = null;

        switch (sortBy) {
            case "Priority":
                comparator = Comparator.comparingInt(task -> {
                    switch (task.getPriority()) {
                        case "High":
                            return 0;
                        case "Medium":
                            return 1;
                        case "Low":
                            return 2;
                        default:
                            return Integer.MAX_VALUE;
                    }
                });
                break;
            case "Name":
                comparator = Comparator.comparing(Task::getTitle);
                break;
            case "Due Date":
                comparator = Comparator.comparing(task -> task.getDueDate());
                break;
            case "Status":
                comparator = Comparator.comparingInt(task -> {
                    switch (task.getStatus()) {
                        case "Overdue":
                            return 0;
                        case "Pending":
                            return 1;
                        case "Completed":
                            return 2;
                        default:
                            return Integer.MAX_VALUE;
                    } // thx cess
                });
                break;
            case "Category":
                comparator = Comparator.comparing(Task::getCategory);
                break;
            default:
                break;

        }

        if (comparator != null) {
            Collections.sort(tasks, comparator);
        }

        // Collections.sort(tasks, Comparator.comparingInt(task -> {
        // switch (task.getPriority()) {
        // case "High":
        // return 0;
        // case "Medium":
        // return 1;
        // case "Low":
        // return 2;
        // default:
        // return Integer.MAX_VALUE;
        // } // this is clever! good job!
        // }));
        if (sortBy.equals("Status")) {
            Collections.sort(tasks, comparator.reversed());
        } else {
            Collections.sort(tasks, comparator);
        }

        for (Task task : manager.getAllTasks()) {
            var localDateTime = LocalDateTime.ofInstant(task.getDueDate().toInstant(), ZoneId.systemDefault());
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MMM dd hh:mm a");
            String formattedDate = localDateTime.format(myFormatObj);           
            tableModel.addRow(new Object[] {
                    task.getId().toString(),
                    task.getCompleted(), // checkbox column
                    task.getTitle(),
                    task.getDescription(),
                    formattedDate,
                    task.getPriority(),
                    task.getPreviousStatus(),
                    task.getStatus(),
                    task.getCategory()
            });
        }
    }

    // main method
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        Font defaultFont = new Font("Montserrat", Font.PLAIN, 12);
        UIManager.setLookAndFeel(new FlatLightLaf());
        UIManager.put("OptionPane.messageFont", new Font("Montserrat", Font.BOLD, 12));
        UIManager.put("OptionPane.buttonFont", defaultFont);
        UIManager.put("OptionPane.font", defaultFont);
        UIManager.put("OptionPane.background", new Color(204, 218, 209));
        UIManager.put("OptionPane.messageForeground", new Color(33, 26, 30));
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        UIManager.put("Spinner.font", defaultFont);
        new MainSushi();
    }
}
