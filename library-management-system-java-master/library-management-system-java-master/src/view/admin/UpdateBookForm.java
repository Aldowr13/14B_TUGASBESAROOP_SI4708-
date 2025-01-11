package view.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UpdateBookForm extends JFrame {
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JTextField titleField, authorField, genreField, yearField;
    private JFrame parentFrame;

    public UpdateBookForm(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setTitle("Update Book Information");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(51, 153, 255)); // Warna biru
        JLabel headerLabel = new JLabel("Update Book Information");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author","Genre", "Year"}, 0);
        booksTable = new JTable(tableModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

        JButton updateButton = new JButton("Update");
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setBackground(new Color(0, 204, 102)); // Warna hijau
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(e -> updateBookInDatabase());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(51, 153, 255)); // Warna biru
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            dispose(); // Menutup View All Books
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
        formPanel.add(updateButton);
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

    private void updateBookInDatabase() {
        int selectedRow = booksTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book from the table!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
            int bookId = (int) tableModel.getValueAt(selectedRow, 0);

            try (Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_management_system", "root", "");
                 PreparedStatement statement = connection.prepareStatement(
                         "UPDATE books SET title = ?, author = ?, genre = ?, year = ? WHERE id = ?")) {

                statement.setString(1, title);
                statement.setString(2, author);
                statement.setString(3, genre);
                statement.setInt(4, year);
                statement.setInt(5, bookId);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadBooksFromDatabase(); // Update tabel setelah update
                    titleField.setText("");
                    authorField.setText("");
                    genreField.setText("");
                    yearField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "No book found with the provided ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Year must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating book: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

