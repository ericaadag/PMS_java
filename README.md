
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



