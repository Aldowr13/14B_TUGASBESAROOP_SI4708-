package view.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DeleteBookForm extends JFrame {
    private JTextField bookIdField;
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JFrame parentFrame;

    public DeleteBookForm(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setTitle("Delete Book");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(204, 0, 51)); // Warna merah tua
        JLabel headerLabel = new JLabel("Delete Book");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author","Genre", "Year"}, 0);
        booksTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(booksTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel bookIdLabel = new JLabel("Book ID:");
        bookIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bookIdField = new JTextField();

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBackground(new Color(255, 51, 51)); // Warna merah
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteBookFromDatabase());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(102, 102, 102)); // Warna abu abu
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> {
            dispose(); // Menutup View All Books
            parentFrame.setVisible(true); // Membuka Main Menu Admin
        });

        formPanel.add(bookIdLabel);
        formPanel.add(bookIdField);
        formPanel.add(deleteButton);
        formPanel.add(cancelButton);

        add(formPanel, BorderLayout.SOUTH);

        loadBooksFromDatabase(); // Load data buku saat aplikasi dijalankan
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

    private void deleteBookFromDatabase() {
        String bookIdText = bookIdField.getText();

        if (bookIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Book ID is required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdText);

            try (Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_management_system", "root", "");
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM books WHERE id = ?")) {

                statement.setInt(1, bookId);
                int rowsDeleted = statement.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    bookIdField.setText("");
                    loadBooksFromDatabase(); // Update tabel setelah penghapusan
                } else {
                    JOptionPane.showMessageDialog(this, "No book found with the provided ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Book ID must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting book: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


