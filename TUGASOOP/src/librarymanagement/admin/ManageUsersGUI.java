package librarymanagement.admin;

import librarymanagement.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManageUsersGUI extends JFrame {
    private JTextField nameField, emailField;
    private JButton addButton, editButton, deleteButton;
    private JTable userTable;
    private List<User> users;
    private int selectedRow;

    public ManageUsersGUI(List<User> users) {
        this.users = users;
        setTitle("Manage Users");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        addButton = new JButton("Add User");
        editButton = new JButton("Edit User");
        deleteButton = new JButton("Delete User");

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);

        add(panel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"ID", "Name", "Email"};
        Object[][] data = new Object[users.size()][3];
        for (int i = 0; i < users.size(); i++) {
            data[i][0] = users.get(i).getId();
            data[i][1] = users.get(i).getName();
            data[i][2] = users.get(i).getEmail();
        }

        userTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button actions
        addButton.addActionListener(e -> addUser());
        editButton.addActionListener(e -> editUser());
        deleteButton.addActionListener(e -> deleteUser());
    }

    private void addUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        if (!name.isEmpty() && !email.isEmpty()) {
            User newUser = new User(users.size() + 1, name, email);
            users.add(newUser);
            updateTable();
        }
    }

    private void editUser() {
        selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            User user = users.get(selectedRow);
            user.setName(nameField.getText());
            user.setEmail(emailField.getText());
            updateTable();
        }
    }

    private void deleteUser() {
        selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            users.remove(selectedRow);
            updateTable();
        }
    }

    private void updateTable() {
        String[] columnNames = {"ID", "Name", "Email"};
        Object[][] data = new Object[users.size()][3];
        for (int i = 0; i < users.size(); i++) {
            data[i][0] = users.get(i).getId();
            data[i][1] = users.get(i).getName();
            data[i][2] = users.get(i).getEmail();
        }

        userTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
}
