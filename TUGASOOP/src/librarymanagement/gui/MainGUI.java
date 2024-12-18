package librarymanagement.gui;

import librarymanagement.admin.AdminGUI;
import librarymanagement.admin.UserGUI;
import librarymanagement.model.Book; // Pastikan Anda mengimpor kelas Book

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainGUI extends JFrame {
    private JButton adminButton, userButton;

    public MainGUI() {
        setTitle("Cassano Library");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Background Image
        JLabel background = new JLabel(new ImageIcon("src/resources/library_background.jpg"));
        background.setLayout(new BorderLayout());
        add(background);

        // Welcome Text
        JLabel welcomeLabel = new JLabel("WELCOME TO CASSANO LIBRARY - FREE", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        background.add(welcomeLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 20));
        buttonPanel.setOpaque(false); // Transparent panel

        adminButton = new JButton("Admin");
        userButton = new JButton("User");

        // Styling Buttons
        adminButton.setFont(new Font("Arial", Font.BOLD, 20));
        userButton.setFont(new Font("Arial", Font.BOLD, 20));
        adminButton.setBackground(new Color(0, 123, 255));
        adminButton.setForeground(Color.WHITE);
        userButton.setBackground(new Color(40, 167, 69));
        userButton.setForeground(Color.WHITE);

        buttonPanel.add(adminButton);
        buttonPanel.add(userButton);

        // Add button panel to the center
        background.add(buttonPanel, BorderLayout.CENTER);

        // Button Actions
        adminButton.addActionListener(e -> {
            AdminGUI adminGUI = new AdminGUI();
            adminGUI.setVisible(true);
            this.dispose();
        });

        userButton.addActionListener(e -> {
            List<Book> books = new ArrayList<>();
            books.add(new Book(1, "Harry Potter", "J.K. Rowling"));
            books.add(new Book(2, "The Hobbit", "J.R.R. Tolkien"));
            books.add(new Book(3, "1984", "George Orwell"));

            UserGUI userGUI = new UserGUI(books);
            userGUI.setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI mainGUI = new MainGUI();
            mainGUI.setVisible(true);
        });
    }
}
