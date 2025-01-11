package view.admin;

import model.User;

import javax.swing.*;
import java.awt.*;

public class MainMenuAdmin extends JFrame {
    private JFrame parentFrame;

    public MainMenuAdmin(
            User user,
            JFrame parentFrame
    ) {
        this.parentFrame = parentFrame;
        setTitle("Admin Main Menu");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel titleLabel = new JLabel("Library Management System - Admin");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 3, 20, 20)); // Grid layout untuk menu
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(Color.WHITE);

        // Menu Buttons
        JButton addUserButton = createMenuButton("Add User");
        JButton viewBooksButton = createMenuButton("View All Books");
        JButton addBookButton = createMenuButton("Add Book");
        JButton deleteBookButton = createMenuButton("Delete Book");
        JButton updateBookButton = createMenuButton("Update Book");
        JButton logoutButton = createMenuButton("Logout");

        // Tambahkan action listener untuk setiap tombol (dummy action)
        addUserButton.addActionListener(e -> {
            dispose(); // Menutup Main Menu Admin
            new AddUserForm(this).setVisible(true); // Membuka View All Books
        });

        // View Book
        viewBooksButton.addActionListener(e -> {
            dispose(); // Menutup Main Menu Admin
            new ViewAllBooks(this).setVisible(true); // Membuka View All Books
        });

        // Add Book
        addBookButton.addActionListener(e -> {
            dispose(); // Menutup Main Menu Admin
            new AddBookForm(this).setVisible(true); // Membuka View All Books
        });

        // Delete Book
        deleteBookButton.addActionListener(e -> {
            dispose(); // Menutup Main Menu Admin
            new DeleteBookForm(this).setVisible(true); // Membuka View All Books
        });

        updateBookButton.addActionListener(e -> {
            dispose(); // Menutup Main Menu Admin
            new UpdateBookForm(this).setVisible(true); // Membuka View All Books
        });
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logged out!");
            dispose(); // Menutup Main Menu Admin
            parentFrame.setVisible(true);
        });

        // Tambahkan tombol ke panel utama
        mainPanel.add(addUserButton);
        mainPanel.add(viewBooksButton);
        mainPanel.add(addBookButton);
        mainPanel.add(deleteBookButton);
        mainPanel.add(updateBookButton);
        mainPanel.add(logoutButton);

        add(mainPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(0, 102, 204));
        JLabel footerLabel = new JLabel("Library Management System © 2025");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(footerLabel);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 153, 76));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 50)); // Ukuran tetap
        return button;
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new MainMenuAdmin().setVisible(true));
//    }
}


