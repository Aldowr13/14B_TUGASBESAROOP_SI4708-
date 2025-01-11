package view.user;

import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReturnBook extends JFrame {
    private JFrame parentFrame;
    private JTable bookTable;
    private Integer user_id;

    public ReturnBook(JFrame parentFrame, User user) {
        this.parentFrame = parentFrame;
        this.user_id = user.getId();

        // Frame settings
        setTitle("Return Book");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 248, 255)); // Light blue background

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(51, 153, 255)); // Warna biru
        JLabel headerLabel = new JLabel("Return Book");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Table for borrowed books
        bookTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Title", "Author", "Borrow Date"}, 0
        ));
        JScrollPane tableScrollPane = new JScrollPane(bookTable);

        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        infoPanel.setBackground(new Color(255, 255, 255)); // White background

        JButton returnButton = new JButton("Return Book");
        returnButton.setBackground(new Color(0, 153, 0));
        returnButton.setForeground(Color.WHITE);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(51, 153, 255));
        cancelButton.setForeground(Color.WHITE);

        infoPanel.add(returnButton);
        infoPanel.add(cancelButton);

        // Action listeners
        returnButton.addActionListener(e -> returnBook());
        cancelButton.addActionListener(e -> {
            dispose(); // Menutup form ReturnBook
            parentFrame.setVisible(true); // Membuka kembali frame sebelumnya
        });

        // Add components to main panel
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        // Add to frame
        add(mainPanel);

        // Load borrowed books from database
        loadBorrowedBooks();
    }

    private void loadBorrowedBooks() {
        DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
        model.setRowCount(0); // Hapus data sebelumnya

        String query = """
            SELECT b.id, b.title, b.author, t.borrow_date
            FROM books b
            JOIN transactions t ON b.id = t.book_id
            WHERE t.user_id = ? AND t.return_date IS NULL
        """;

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_management_system", "root", "");
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    Date borrowDate = resultSet.getDate("borrow_date");

                    model.addRow(new Object[]{id, title, author, borrowDate});
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void returnBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to return.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) bookTable.getValueAt(selectedRow, 0);
        String bookTitle = bookTable.getValueAt(selectedRow, 1).toString();

        String updateTransaction = "UPDATE transactions SET return_date = NOW() WHERE user_id = ? AND book_id = ? AND return_date IS NULL";

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_management_system", "root", "");
             PreparedStatement statement = connection.prepareStatement(updateTransaction)) {

            // Update return date
            statement.setInt(1, user_id);
            statement.setInt(2, bookId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "You have successfully returned: " + bookTitle, "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBorrowedBooks(); // Refresh daftar buku yang belum dikembalikan
            } else {
                JOptionPane.showMessageDialog(this, "Failed to return the book. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

