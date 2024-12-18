package librarymanagement.admin;

import librarymanagement.model.Book;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManageBooksGUI extends JFrame {
    private JTextField titleField, authorField;
    private JButton addButton, editButton, deleteButton, backButton;
    private JTable bookTable;
    private List<Book> books;
    private int selectedRow;

    public ManageBooksGUI(List<Book> books) {
        this.books = books;
        setTitle("Manage Books");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));  // Adjusted to add backButton

        panel.add(new JLabel("Title:"));
        titleField = new JTextField();
        panel.add(titleField);

        panel.add(new JLabel("Author:"));
        authorField = new JTextField();
        panel.add(authorField);

        addButton = new JButton("Add Book");
        editButton = new JButton("Edit Book");
        deleteButton = new JButton("Delete Book");
        backButton = new JButton("Back"); // New button for back functionality

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(backButton); // Add the back button to the panel

        add(panel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"ID", "Title", "Author"};
        Object[][] data = new Object[books.size()][3];
        for (int i = 0; i < books.size(); i++) {
            data[i][0] = books.get(i).getId();
            data[i][1] = books.get(i).getTitle();
            data[i][2] = books.get(i).getAuthor();
        }

        bookTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button actions
        addButton.addActionListener(e -> addBook());
        editButton.addActionListener(e -> editBook());
        deleteButton.addActionListener(e -> deleteBook());
        backButton.addActionListener(e -> goBack()); // Action for back button
    }

    private void addBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        if (!title.isEmpty() && !author.isEmpty()) {
            Book newBook = new Book(books.size() + 1, title, author);
            books.add(newBook);
            updateTable();
        }
    }

    private void editBook() {
        selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            Book book = books.get(selectedRow);
            book.setTitle(titleField.getText());
            book.setAuthor(authorField.getText());
            updateTable();
        }
    }

    private void deleteBook() {
        selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            books.remove(selectedRow);
            updateTable();
        }
    }

    private void goBack() {
        // Close current window and return to Admin GUI
        this.dispose();
        AdminGUI adminGUI = new AdminGUI();
        adminGUI.setVisible(true);
    }

    private void updateTable() {
        String[] columnNames = {"ID", "Title", "Author"};
        Object[][] data = new Object[books.size()][3];
        for (int i = 0; i < books.size(); i++) {
            data[i][0] = books.get(i).getId();
            data[i][1] = books.get(i).getTitle();
            data[i][2] = books.get(i).getAuthor();
        }

        bookTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
}

