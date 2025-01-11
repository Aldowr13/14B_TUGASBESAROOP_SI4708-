package view.user;

import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BorrowBook extends JFrame {
    private JFrame parentFrame;
    private JTable bookTable;
    private JTextField searchField;
    private Integer user_id; // Username pengguna yang sedang login

    public BorrowBook(JFrame parentFrame, User user) {
        this.parentFrame = parentFrame;
        this.user_id = user.getId();

        // Frame settings
        setTitle("Borrow Book");
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
        JLabel headerLabel = new JLabel("Borrow Book");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBackground(new Color(240, 248, 255));

        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField();
        JButton searchButton = new JButton("Find");
        searchButton.setBackground(new Color(0, 102, 204));
        searchButton.setForeground(Color.WHITE);

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Table for books
        bookTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Title", "Author", "Genre", "Year"}, 0
        ));
        JScrollPane tableScrollPane = new JScrollPane(bookTable);

        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        infoPanel.setBackground(new Color(255, 255, 255)); // White background

        JButton borrowButton = new JButton("Borrow Book");
        borrowButton.setBackground(new Color(0, 153, 0));
        borrowButton.setForeground(Color.WHITE);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(51, 153, 255));
        cancelButton.setForeground(Color.WHITE);

        infoPanel.add(borrowButton);
        infoPanel.add(cancelButton);

        // Action listeners
        borrowButton.addActionListener(e -> borrowBook());
        cancelButton.addActionListener(e -> {
            dispose(); // Menutup form AddBook
            parentFrame.setVisible(true); // Membuka Main Menu Admin
        });
        searchButton.addActionListener(e -> searchBooks());

        // Add components to main panel
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        // Add to frame
        add(mainPanel);

        // Load books from database
        loadBooks();
    }

    private void loadBooks() {
        DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
        model.setRowCount(0); // Hapus data sebelumnya

        String query = "SELECT * FROM books WHERE available = 1";

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_management_system", "root", "");
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                int year = resultSet.getInt("year");
                model.addRow(new Object[]{id, title, author, genre, year});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchBooks() {
        String searchText = searchField.getText().trim().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) bookTable.getModel();

        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter text to search!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            String title = model.getValueAt(i, 1).toString().toLowerCase();
            if (title.contains(searchText)) {
                bookTable.setRowSelectionInterval(i, i);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "No books found matching your search.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void borrowBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to borrow.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) bookTable.getValueAt(selectedRow, 0);
        String bookTitle = bookTable.getValueAt(selectedRow, 1).toString();

        String insertTransaction = "INSERT INTO transactions (user_id, book_id, borrow_date) VALUES (?, ?, NOW())";

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_management_system", "root", "");
             PreparedStatement transactionStmt = connection.prepareStatement(insertTransaction)) {

            // Tambahkan transaksi
            transactionStmt.setInt(1, user_id);
            transactionStmt.setInt(2, bookId);
            transactionStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "You have successfully borrowed: " + bookTitle, "Success", JOptionPane.INFORMATION_MESSAGE);
            loadBooks(); // Refresh daftar buku

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
