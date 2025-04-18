/* 
FUNCTIONALITY
1. The code is a payroll management System which has the concept of CRUD- Create, Read, Update, Delete applied using object-oriented Principles in java. It features a java mainframe GUI and is connected to a database for data (Employee) management. Thus, there is the ability to add new employees, update existing employee details, delete employee records and search for an employee by name.

2. The code initialises a graphical user interface for a login screen using Java Swing. The main method launches the application by creating an instance of the LoginFrame class. The LoginFrame class sets up a simple login form with fields for username and password.

3. There is also an employee processing where payroll information such as tax and new salary are calculated and displayed.
      	This also allows exporting the payroll summary to a        	text file.
This is achieved through a series of interconnected GUI components and event handlers that process user input, display data in tables and handle file I/O.

LIBRARIES AND DEPENDENCIES BEING USED
1. Javax.swing.*: This provides classes for building the GUI, such as JFrame, Label, JTextField, and JPasswordField. These are used to create and manage the login form.
2. javax.swing.table.DefaultTableModel: Typically used for managing data in table components (JTable).
3. java.awt.*: Provides layout managers like GridLayout to organize components in the GUI.
4. java.awt.event.*: Used for handling user interactions like button clicks.
5. java.io.FileWriter and java.io.IOException: java.io.FileWriter writes the payroll summary to text file, where java.io.IOException handles exceptions when writing files.
6. java.time.LocalDateTime and java.time.format.DateTimeFormatter:Captures the current date and time for payroll summary and the later is used for handling and formatting date/time values in a readable way.

COMPONENTS AND STRUCTURE
1. PayrollManagementSystem (Main Class)
* Launches the application with the LoginFrame on the Event Dispatch Thread using SwingUtilities.invokeLater().

2. LoginFrame
* GUI window prompting user credentials.
* If correct (admin/password), opens the main application (MainFrame).
* Uses:
    * JTextField, JPasswordField for input
    * JButton for login
    * JOptionPane for validation feedback

3. MainFrame
* Main window after login.
* Uses a JTabbedPane with:
    * Tab 1: EmployeeManagementForm – manages employee records
    * Tab 2: PayrollProcessingForm – handles payroll operations

4. EmployeeManagementForm
* Displays employee data in a table (JTable) backed by a DefaultTableModel.
* Includes buttons for:
    * Add: Opens EmployeeForm dialog to input new employee.
    * Update: Edits selected employee's details.
    * Delete: Removes selected employee.
    * Search: Locates employee by name.

5. EmployeeForm
* A dialog window (JDialog) to input a new employee's:
    * ID
    * Name
    * Position
    * Salary
* On "Save", it sends the info to EmployeeManagementForm via addEmployee().

6. PayrollProcessingForm
* Accesses employee data via the reference to EmployeeManagementForm.
* Allows:
    * Generate Payroll:
        * Iterates over all employees
        * Applies a 10% tax rate
        * Calculates net salary
        * Adds timestamp
    * Download Summary:
        * Saves generated payroll text to Payroll_Statement.txt using FileWriter.



 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PayrollManagementSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}

class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Login");
        add(loginButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.equals("admin") && password.equals("password")) {
                dispose();
                new MainFrame();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}

class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private EmployeeManagementForm employeeForm;
    private PayrollProcessingForm payrollForm;

    public MainFrame() {
        setTitle("Payroll Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();
        employeeForm = new EmployeeManagementForm();
        payrollForm = new PayrollProcessingForm(employeeForm);

        tabbedPane.addTab("Employee Management", employeeForm);
        tabbedPane.addTab("Payroll Processing", payrollForm);

        add(tabbedPane);
        setVisible(true);
    }
}

class EmployeeManagementForm extends JPanel {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton addButton, updateButton, deleteButton, searchButton;

    public EmployeeManagementForm() {
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Name", "Position", "Salary"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Employee");
        updateButton = new JButton("Update Employee");
        deleteButton = new JButton("Delete Employee");
        searchButton = new JButton("Search Employee");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> new EmployeeForm(this, "Add"));
        updateButton.addActionListener(e -> updateEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());
        searchButton.addActionListener(e -> searchEmployee());
    }

    public void addEmployee(String id, String name, String position, double salary) {
        tableModel.addRow(new Object[]{id, name, position, salary});
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    private void updateEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = JOptionPane.showInputDialog("Enter new ID:", tableModel.getValueAt(selectedRow, 0));
            String name = JOptionPane.showInputDialog("Enter new Name:", tableModel.getValueAt(selectedRow, 1));
            String position = JOptionPane.showInputDialog("Enter new Position:", tableModel.getValueAt(selectedRow, 2));
            String salaryStr = JOptionPane.showInputDialog("Enter new Salary:", tableModel.getValueAt(selectedRow, 3));

            if (id != null && name != null && position != null && salaryStr != null) {
                tableModel.setValueAt(id, selectedRow, 0);
                tableModel.setValueAt(name, selectedRow, 1);
                tableModel.setValueAt(position, selectedRow, 2);
                tableModel.setValueAt(Double.parseDouble(salaryStr), selectedRow, 3);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select an employee to update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Select an employee to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void searchEmployee() {
        String name = JOptionPane.showInputDialog(this, "Enter employee name to search:");
        if (name != null) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 1).toString().equalsIgnoreCase(name)) {
                    employeeTable.setRowSelectionInterval(i, i);
                    JOptionPane.showMessageDialog(this, "Employee found: " + name, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Employee not found.", "Search", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

class EmployeeForm extends JDialog {
    private JTextField idField, nameField, positionField, salaryField;
    private JButton saveButton;
    
    public EmployeeForm(EmployeeManagementForm parent, String action) {
        super((JFrame) SwingUtilities.getWindowAncestor(parent), action + " Employee", true);
        setSize(300, 200);
        setLayout(new GridLayout(5, 2));
        
        add(new JLabel("ID:"));
        idField = new JTextField();
        add(idField);
        
        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);
        
        add(new JLabel("Position:"));
        positionField = new JTextField();
        add(positionField);
        
        add(new JLabel("Salary:"));
        salaryField = new JTextField();
        add(salaryField);
        
        saveButton = new JButton("Save");
        add(saveButton);
        
        saveButton.addActionListener(e -> {
            parent.addEmployee(idField.getText(), nameField.getText(), positionField.getText(), Double.parseDouble(salaryField.getText()));
            dispose();
        });
        
        setVisible(true);
    }
}


class PayrollProcessingForm extends JPanel {
    private JTextArea payrollSummary;
    private JButton generateButton, downloadButton;
    private EmployeeManagementForm employeeForm;

    public PayrollProcessingForm(EmployeeManagementForm employeeForm) {
        this.employeeForm = employeeForm;
        setLayout(new BorderLayout());

        payrollSummary = new JTextArea("Payroll Summary Will Appear Here");
        add(new JScrollPane(payrollSummary), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        generateButton = new JButton("Generate Payroll");
        downloadButton = new JButton("Download Payroll Summary");

        buttonPanel.add(generateButton);
        buttonPanel.add(downloadButton);
        add(buttonPanel, BorderLayout.SOUTH);

        generateButton.addActionListener(e -> generatePayrollSummary());
        downloadButton.addActionListener(e -> downloadPayrollStatement());
    }

    private void generatePayrollSummary() {
        DefaultTableModel tableModel = employeeForm.getTableModel();
        StringBuilder summary = new StringBuilder("Payroll Statement\n\n");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);

        summary.append("Date Generated: ").append(date).append("\n\n");

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String name = tableModel.getValueAt(i, 1).toString();
            double salary = Double.parseDouble(tableModel.getValueAt(i, 3).toString());
            double tax = salary * 0.1;  // 10% Tax
            double netSalary = salary - tax;

            summary.append("Employee: ").append(name).append("\n")
                    .append("Gross Salary: $").append(salary).append("\n")
                    .append("Tax (10%): $").append(tax).append("\n")
                    .append("Net Salary: $").append(netSalary).append("\n")
                    .append("--------------------------------------\n");
        }

        payrollSummary.setText(summary.toString());
    }

    private void downloadPayrollStatement() {
        try (FileWriter writer = new FileWriter("Payroll_Statement.txt")) {
            writer.write(payrollSummary.getText());
            JOptionPane.showMessageDialog(this, "Payroll statement downloaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}