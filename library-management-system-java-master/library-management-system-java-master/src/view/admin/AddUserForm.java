package view.admin;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddUserForm extends JFrame {
    private JTextField emailField, usernameField;
    private JPasswordField passwordField;
    private JFrame parentFrame;

    public AddUserForm(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setTitle("Add New User");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(51, 153, 255)); // Warna biru
        JLabel headerLabel = new JLabel("Add New User");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailField = new JTextField();

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordField = new JPasswordField();

        JButton addButton = new JButton("Add User");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(0, 204, 102)); // Warna hijau
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addUserToDatabase());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(51, 153, 255)); // Warna biru
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            dispose(); // Menutup form AddUser
            parentFrame.setVisible(true); // Membuka Main Menu Admin
        });

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(addButton);
        formPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
    }

    private void addUserToDatabase() {
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validasi email domain
        if (!email.matches("^[\\w.%+-]+@telkomuniversity\\.ac\\.id$")) {
            JOptionPane.showMessageDialog(this, "Email must be a valid campus email (e.g., user@telkomuniversity.ac.id)!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_management_system", "root", "");
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO users (email, username, password) VALUES (?, ?, ?)")) {

            statement.setString(1, email);
            statement.setString(2, username);
            statement.setString(3, password);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                parentFrame.setVisible(true);
                emailField.setText("");
                usernameField.setText("");
                passwordField.setText("");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
