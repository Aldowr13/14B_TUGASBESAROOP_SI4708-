package view.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AddBookForm extends JFrame {
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JTextField titleField, authorField, genreField, yearField;
    private JFrame parentFrame;

    public AddBookForm(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setTitle("Add New Book");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(51, 153, 255)); // Warna biru
        JLabel headerLabel = new JLabel("Add New Book");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Table Panel (dummy, no data needed for "Add Book")
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Genre", "Year"}, 0);
        booksTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(booksTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleField = new JTextField();

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        authorField = new JTextField();

        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        genreField = new JTextField();

        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setFont(new Font("Arial", Font.BOLD, 14));
        yearField = new JTextField();

        JButton addButton = new JButton("Add Book");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(0, 204, 102)); // Warna hijau
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addBookToDatabase());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(51, 153, 255)); // Warna biru
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            dispose(); // Menutup form AddBook
            parentFrame.setVisible(true); // Membuka Main Menu Admin
        });

        formPanel.add(titleLabel);
        formPanel.add(titleField);
        formPanel.add(authorLabel);
        formPanel.add(authorField);
        formPanel.add(genreLabel);
        formPanel.add(genreField);
        formPanel.add(yearLabel);
        formPanel.add(yearField);
        formPanel.add(addButton);
        formPanel.add(cancelButton);

        add(formPanel, BorderLayout.SOUTH);

        loadBooksFromDatabase();
    }

    private void loadBooksFromDatabase() {
        tableModel.setRowCount(0); // Hapus data lama dari tabel

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_management_system", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM books")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                int year = resultSet.getInt("year");
                tableModel.addRow(new Object[]{id, title, author, genre, year});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addBookToDatabase() {
        String title = titleField.getText();
        String author = authorField.getText();
        String genre = genreField.getText();
        String yearText = yearField.getText();

        if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || yearText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int year = Integer.parseInt(yearText);

            try (Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_management_system", "root", "");
                 PreparedStatement statement = connection.prepareStatement(
                         "INSERT INTO books (title, author, genre, year) VALUES (?, ?, ?, ?)")) {

                statement.setString(1, title);
                statement.setString(2, author);
                statement.setString(3, genre);
                statement.setInt(4, year);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadBooksFromDatabase();
                    titleField.setText("");
                    authorField.setText("");
                    genreField.setText("");
                    yearField.setText("");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Year must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding book: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

