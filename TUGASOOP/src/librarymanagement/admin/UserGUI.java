package librarymanagement.admin;

import librarymanagement.model.Book;

import javax.swing.*;
import java.awt.*;
import java.util.List;
public class UserGUI extends JFrame {

    private JButton borrowBookButton, returnBookButton, logoutButton;

    public UserGUI(List<Book> books) {
        setTitle("User Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Background Image
        JLabel background = new JLabel(new ImageIcon("src/resources/library_background.jpg"));
        background.setLayout(new BorderLayout());
        add(background);

        // Header Text
        JLabel headerLabel = new JLabel("WELCOME TO USER DASHBOARD", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        background.add(headerLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 20, 20));
        buttonPanel.setOpaque(false);

        borrowBookButton = new JButton("Borrow Book");
        returnBookButton = new JButton("Return Book");
        logoutButton = new JButton("Logout");

        // Styling Buttons
        styleButton(borrowBookButton, new Color(40, 167, 69));
        styleButton(returnBookButton, new Color(23, 162, 184));
        styleButton(logoutButton, new Color(220, 53, 69));

        buttonPanel.add(borrowBookButton);
        buttonPanel.add(returnBookButton);
        buttonPanel.add(logoutButton);

        background.add(buttonPanel, BorderLayout.CENTER);

        // Button Actions
        borrowBookButton.addActionListener(e -> {
            String message = "Books Available:\n";
            for (Book book : books) {
                message += "ID: " + book.getId() + " | Title: " + book.getTitle() + " | Author: " + book.getAuthor() + "\n";
            }
            JOptionPane.showMessageDialog(this, message);
        });

        returnBookButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Return Book clicked!"));

        logoutButton.addActionListener(e -> {
            this.dispose();
            new librarymanagement.gui.MainGUI().setVisible(true);
        });
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }
}
