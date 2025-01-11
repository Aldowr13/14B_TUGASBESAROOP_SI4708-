package view;

import controller.AuthController;
import model.User;
import view.user.MainMenuUser;
import view.admin.MainMenuAdmin;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginUI() {
        setTitle("Library Management System - Login");
        setSize(400, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(51, 153, 255)); // Warna biru
        JLabel headerLabel = new JLabel("Library Management System");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(null); // Menggunakan null layout
        formPanel.setPreferredSize(new Dimension(400, 150));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 20, 100, 25);
        usernameField = new JTextField();
        usernameField.setBounds(150, 20, 200, 25);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 60, 100, 25);
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 60, 200, 25);

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 153, 76));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(100, 30));

        loginButton.addActionListener(e -> handleLogin());

        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Autentikasi melalui AuthController
        User user = AuthController.authenticate(username, password);

        if (user != null) {
            if (user.getRole().equals("admin")) {
                new MainMenuAdmin(user, this).setVisible(true); // Pass user object
            } else if (user.getRole().equals("user")) {
                new MainMenuUser(user, this).setVisible(true); // Pass user object
            }
            usernameField.setText("");
            passwordField.setText("");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}
