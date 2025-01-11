package controller;

import model.User;

import java.sql.*;

public class AuthController {
    public static User authenticate(String username, String password) {
        String query = "SELECT id, role FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_management_system", "root", "");
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String role = resultSet.getString("role");
                    return new User(id, username, role); // Mengembalikan objek User
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Jika login gagal, kembalikan null
    }
}
