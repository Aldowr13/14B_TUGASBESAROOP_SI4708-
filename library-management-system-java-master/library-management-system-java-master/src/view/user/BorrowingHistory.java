package view.user;

import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BorrowingHistory extends JFrame {
    private JFrame parentFrame;
    private JTable bookTable;
    private Integer user_id;

    public BorrowingHistory(JFrame parentFrame, User user) {
        this.parentFrame = parentFrame;
        this.user_id = user.getId();

        // Frame settings
        setTitle("Borrowing History");
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
        JLabel headerLabel = new JLabel("Borrowing History");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Table for borrowing history
        bookTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Title", "Author", "Borrow Date", "Return Date"}, 0
        ));
        JScrollPane tableScrollPane = new JScrollPane(bookTable);

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        infoPanel.setBackground(new Color(255, 255, 255)); // White background

        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(51, 153, 255));
        closeButton.setForeground(Color.WHITE);

        infoPanel.add(closeButton);

        // Action listeners
        closeButton.addActionListener(e -> {
            dispose(); // Menutup form BorrowingHistory
            parentFrame.setVisible(true); // Membuka kembali frame sebelumnya
        });

        // Add components to main panel
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        // Add to frame
        add(mainPanel);

        // Load borrowing history from database
        loadBorrowingHistory();
    }

    private void loadBorrowingHistory() {
        DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
        model.setRowCount(0); // Hapus data sebelumnya

        String query = """
            SELECT b.id, b.title, b.author, t.borrow_date, t.return_date
            FROM books b
            JOIN transactions t ON b.id = t.book_id
            WHERE t.user_id = ?
            ORDER BY t.return_date IS NULL, t.return_date ASC
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
                    Date returnDate = resultSet.getDate("return_date");

                    model.addRow(new Object[]{id, title, author, borrowDate, returnDate == null ? "Not Returned" : returnDate.toString()});
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

