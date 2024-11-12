// importz
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MainSushi {
    // instantiating Swing GUI shit
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private final TaskManager manager;
    private final JFrame mainFrame;
    private UserManager userManager;

    public MainSushi() {
        // main frame settings
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
        
        // user stuff
        userManager = new UserManager();
        if (!loginWindow()) {
            JOptionPane.showMessageDialog(null, "Login failed.");
            System.exit(0);
        }
    }

    private boolean loginWindow() {
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        int option = JOptionPane.showConfirmDialog(null, new Object[] {
            "Username:", usernameField, 
            "Password:", passwordField
        }, "Login", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // login attempt OR register attempt if the user is "new"
            if (!userManager.loginUser(username, password)) {
                int registerOption = JOptionPane.showConfirmDialog(null, "User not found. Would you like to register a new account?", "Register", JOptionPane.NO_OPTION);

                if (registerOption == JOptionPane.YES_OPTION) {
                    if (userManager.registerUser(username, password)) {
                        return userManager.loginUser(username, password);  // logs in after reg
                    } else {
                        JOptionPane.showMessageDialog(null, "Could not register. Please try again.");
                    }
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
        return false;
    }

    private void swingGUI() {
        // making the main frame's panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.decode("#CCDAD1")); 
        tableModel = new DefaultTableModel(
                new String[] { "Title", "Description", "Due Date", "Priority", "Status", "Category" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

            // TABLE
        // making the table that the tasks will be in
        taskTable = new JTable(tableModel);

        // table and header attributes
        taskTable.setFont(new Font("Montserrat", Font.PLAIN, 12));
        taskTable.setIntercellSpacing(new Dimension(0, 0));
        taskTable.getTableHeader().setBackground(Color.decode("#211A1E"));
        taskTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        taskTable.setBackground(Color.decode("#211A1E"));
        taskTable.setFillsViewportHeight(true);
        // UNREASONABLY COMPLICATED BORDER FOR THE TABLE
        taskTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                ((JLabel) comp).setHorizontalAlignment(SwingConstants.CENTER);
                ((JLabel) comp).setFont(new Font("Montserrat", Font.PLAIN, 16));
                ((JLabel) comp).setForeground(Color.decode("#CCDAD1"));
                if (column < table.getColumnCount() - 1) { // needed it to only paint borders on specific cells like on
                                                           // the last or first one, so that in-betweens can be generic
                    ((JComponent) comp).setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.decode("#211A1E")));
                } else {
                    ((JComponent) comp).setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.decode("#211A1E")));
                }

                return comp;
            }
        });
        //
        // adds the table to a scrollpane panel then the scrollpane to a separate panel
        taskTable.setComponentPopupMenu(createTablePopupMenu());
        JScrollPane tableScrollPane = new JScrollPane(taskTable);
        tableScrollPane.setPreferredSize(new Dimension(900, 400));
        tableScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        tableScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        JPanel spacerPanel = new JPanel();
        spacerPanel.setPreferredSize(new Dimension(700, 30));
        spacerPanel.setBackground(Color.decode("#CCDAD1"));
        mainPanel.add(spacerPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        tableScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        // sets the border for table elements (also unreasonably complex)
        taskTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
                ((JLabel) comp).setHorizontalAlignment(SwingConstants.CENTER);
        
                ((JComponent) comp).setBorder(BorderFactory.createMatteBorder(
                    1, 1, (row == table.getRowCount() - 1 ? 1 : 0), 1, Color.decode("#211A1E")
                ));
        
                Color alternateColor = Color.decode("#B5CBBC");
                Color defaultColor = Color.decode("#CCDAD1");
        
                if (!isSelected) {
                    comp.setBackground(row % 2 == 0 ? alternateColor : defaultColor);
                } else {
                    comp.setBackground(Color.decode("#35524A"));
                }
        
                return comp;
            }
        });
        //
            // END OF TABLE

        // adds the mainPanel (which contains the table and topPanel) to the JFrame
        mainFrame.add(mainPanel, BorderLayout.CENTER);

        // title panel at the top
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.decode("#211A1E"));
        titlePanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 70));

        // label for title (left-aligned with padding on the left)
        JLabel title = new JLabel("Sushi Beta 1.0 ", SwingConstants.LEFT);
        // ImageIcon logo = new ImageIcon("C:/Users/hanse/Desktop/CCS0023-FINALS-REPO/CCS0023-FINALS/assets/sushi-logo2.png");
        ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource("assets/sushi-logo2.png"));
        title.setIcon(logo);
        title.setIconTextGap(10);
        title.setFont(new Font("Montserrat", Font.ITALIC, 24));
        title.setForeground(Color.decode("#FF8552"));
        title.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

        titlePanel.add(title, BorderLayout.WEST);

        // wraps the button in another panel within the title panel because i'm the GOAT
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 15));
        buttonPanel.setBackground(Color.decode("#211A1E"));
        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> addTaskGUI());
        addButton.setFont(new Font("Montserrat", Font.BOLD, 14));
        addButton.setPreferredSize(new Dimension(120, 40));
        addButton.setBackground(Color.decode("#211A1E"));
        addButton.setForeground(Color.decode("#CCDAD1"));
        addButton.setFocusable(false);

        // adds button to the buttonPanel and adds buttonPanel to the right of the title
        // because CSS has never left my soul
        buttonPanel.add(addButton);
        titlePanel.add(buttonPanel, BorderLayout.EAST);

        // adds title panel to the main frame at the top
        mainFrame.add(titlePanel, BorderLayout.NORTH);

        // ensures that the frame is updated accordingly
        mainFrame.revalidate();
        mainFrame.repaint();

        refreshTaskTable();
    }

    // right click menu
    private JPopupMenu createTablePopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(e -> editSelectedTask());

        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(e -> deleteSelectedTask());

        popupMenu.add(editItem);
        popupMenu.add(deleteItem);

        return popupMenu;
    }

    // method that is called when you click the add task button (has the UI for the
    // task adding, will customize later0
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
        }, "Add Task", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String priority = (String) priorityBox.getSelectedItem();
            String status = (String) statusBox.getSelectedItem();
            Date dueDate = (Date) dueDateSpinner.getValue();
            String category = categoryField.getText();

            Tasks task = new Tasks(title, description, dueDate, priority, status, category);
            manager.addTask(task);
            refreshTaskTable();
            JOptionPane.showMessageDialog(mainFrame, "Task added successfully.");
        }
    }

    // self explanatory
    private void deleteSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            String title = (String) taskTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to delete this task?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    manager.removeTask(title);
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

    // self explanatory
    private void editSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            String title = (String) taskTable.getValueAt(selectedRow, 0);
            Tasks task = manager.getTaskByTitle(title);

            if (task != null) {
                JTextField titleField = new JTextField(task.getTitle(), 10);
                JTextField descriptionField = new JTextField(task.getDescription(), 10);
                JComboBox<String> priorityBox = new JComboBox<>(new String[] { "High", "Medium", "Low" });
                priorityBox.setSelectedItem(task.getPriority());
                JComboBox<String> statusBox = new JComboBox<>(new String[] { "Pending", "Complete", "Overdue" });
                statusBox.setSelectedItem(task.getStatus());
                JSpinner dueDateSpinner = new JSpinner(
                        new SpinnerDateModel(task.getDueDate(), null, null, java.util.Calendar.DAY_OF_MONTH)); // need
                                                                                                               // to
                                                                                                               // change
                                                                                                               // this
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
                }, "Edit Task", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    task.setTitle(titleField.getText());
                    task.setDescription(descriptionField.getText());
                    task.setPriority((String) priorityBox.getSelectedItem());
                    task.setStatus((String) statusBox.getSelectedItem());
                    task.setDueDate((Date) dueDateSpinner.getValue());
                    task.setCategory(categoryField.getText());
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

    // refreshes every time a task has been edited, added, OR deleted
    private void refreshTaskTable() {
        tableModel.setRowCount(0);

        for (Tasks task : manager.getAllTasks()) {
            var localDateTime = LocalDateTime.ofInstant(task.getDueDate().toInstant(), ZoneId.systemDefault());
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MMM dd hh:mm a");
            String formattedDate = localDateTime.format(myFormatObj);
            tableModel.addRow(new Object[] {
                    task.getTitle(),
                    task.getDescription(),
                    formattedDate,
                    task.getPriority(),
                    task.getStatus(),
                    task.getCategory()
            });
        }
    }

    // main method
    public static void main(String[] args) {
        new MainSushi();
    }
}