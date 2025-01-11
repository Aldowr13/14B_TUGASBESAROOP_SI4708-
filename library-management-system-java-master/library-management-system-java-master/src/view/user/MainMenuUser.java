package view.user;

import model.User;

import javax.swing.*;
import java.awt.*;

public class MainMenuUser extends JFrame {
    private JFrame parentFrame;

    public MainMenuUser(
            User user,
            JFrame parentFrame
    ) {
        this.parentFrame = parentFrame;
        setTitle("User Main Menu");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(102, 153, 255)); // Warna biru muda
        JLabel titleLabel = new JLabel("Library Management System - User");
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
        JButton borrowBookButton = createMenuButton("Borrow Book");
        JButton returnBookButton = createMenuButton("Return Book");
        JButton viewAvailableBooksButton = createMenuButton("Available Books");
        JButton viewHistoryButton = createMenuButton("Borrowing History");
        JButton logoutButton = createMenuButton("Logout");

        // Tambahkan action listener untuk setiap tombol (dummy action)
        borrowBookButton.addActionListener(e -> {
            dispose(); // Menutup Main Menu Admin
            new BorrowBook(this, user).setVisible(true); // Membuka View All Books
        });
        returnBookButton.addActionListener(e -> {
            dispose(); // Menutup Main Menu Admin
            new ReturnBook(this, user).setVisible(true); // Membuka View All Books
        });
        viewAvailableBooksButton.addActionListener(e -> {
            dispose(); // Menutup Main Menu Admin
            new ViewAvailableBooks(this).setVisible(true); // Membuka View All Books
        });
        viewHistoryButton.addActionListener(e -> {
            dispose(); // Menutup Main Menu Admin
            new BorrowingHistory(this, user).setVisible(true); // Membuka View All Books
        });
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logged out!");
            dispose(); // Menutup Main Menu User
            parentFrame.setVisible(true);
        });

        // Tambahkan tombol ke panel utama
        mainPanel.add(borrowBookButton);
        mainPanel.add(returnBookButton);
        mainPanel.add(viewAvailableBooksButton);
        mainPanel.add(viewHistoryButton);
        mainPanel.add(logoutButton);

        add(mainPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(102, 153, 255)); // Warna biru muda
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
        button.setBackground(new Color(0, 204, 153)); // Warna hijau terang untuk user
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 50)); // Ukuran tetap
        return button;
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new MainMenuUser().setVisible(true));
//    }
}


