package librarymanagement.admin;

import librarymanagement.model.Book;
import librarymanagement.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdminGUI extends JFrame {
    private JButton manageBooksButton, manageUsersButton;
    private List<Book> books;
    private List<User> users;

    public AdminGUI() {
        setTitle("Admin Panel - Cassano Library");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize sample data for books and users
        books = new ArrayList<>();
        users = new ArrayList<>();
        books.add(new Book(1, "Java Programming", "John Doe"));
        books.add(new Book(2, "Data Structures", "Jane Smith"));

        users.add(new User(1, "Alice", "alice@example.com"));
        users.add(new User(2, "Bob", "bob@example.com"));

        // Create Buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));

        manageBooksButton = new JButton("Manage Books");
        manageUsersButton = new JButton("Manage Users");

        panel.add(manageBooksButton);
        panel.add(manageUsersButton);

        add(panel, BorderLayout.CENTER);

        // Add Action Listeners for buttons
        manageBooksButton.addActionListener(e -> {
            ManageBooksGUI manageBooksGUI = new ManageBooksGUI(books);
            manageBooksGUI.setVisible(true);
        });

        manageUsersButton.addActionListener(e -> {
            ManageUsersGUI manageUsersGUI = new ManageUsersGUI(users);
            manageUsersGUI.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminGUI adminGUI = new AdminGUI();
            adminGUI.setVisible(true);
        });
    }
}
