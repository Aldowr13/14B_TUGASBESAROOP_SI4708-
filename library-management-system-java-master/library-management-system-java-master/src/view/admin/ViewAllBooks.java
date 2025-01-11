package view.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewAllBooks extends JFrame {
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JFrame parentFrame; // Referensi ke Main Menu Admin

    public ViewAllBooks(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setTitle("View All Books");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204)); // Warna biru
        JLabel headerLabel = new JLabel("Library Books");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Table Panel
        tableModel = new DefaultTableModel();
        booksTable = new JTable(tableModel);

        // Menambahkan kolom ke tabel
        tableModel.addColumn("Book ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("Genre");
        tableModel.addColumn("Year");

        // Styling tabel
        booksTable.setFont(new Font("Arial", Font.PLAIN, 14));
        booksTable.setRowHeight(25);
        booksTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        booksTable.getTableHeader().setBackground(new Color(0, 102, 204));
        booksTable.getTableHeader().setForeground(Color.WHITE);
        booksTable.setGridColor(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(booksTable);
        add(scrollPane, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(0, 102, 204));

        // Back Button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(204, 0, 0)); // Merah untuk tombol back
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            dispose(); // Menutup View All Books
            parentFrame.setVisible(true); // Membuka Main Menu Admin
        });

        footerPanel.add(backButton, BorderLayout.WEST);
        JLabel footerLabel = new JLabel("Library Management System © 2025", JLabel.CENTER);
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(footerLabel, BorderLayout.CENTER);

        add(footerPanel, BorderLayout.SOUTH);

        // Load data dari database
        loadBooksFromDatabase();
    }

    private void loadBooksFromDatabase() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Koneksi ke database
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_management_system", "root", "");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM books");

            // Mengambil data dari database dan menambahkannya ke tabel
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
        } finally {
            // Menutup koneksi
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


