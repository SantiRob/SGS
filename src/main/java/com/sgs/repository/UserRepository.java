package com.sgs.repository;

import com.sgs.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final Connection connection = DatabaseConnection.getConnection();

    public User findByEmailAndPassword(String email, String password) {
        String sql = "SELECT id_user, name, email, role, password FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id_user"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("password")
                );
            }
        } catch (Exception e) {
            System.err.println("Error al buscar usuario:");
            e.printStackTrace();
        }
        return null;
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void save(User user) {
        String sql = "INSERT INTO users (name, email, role, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getPassword());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al registrar usuario:");
            e.printStackTrace();
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id_user"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("password")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean deleteById(int id) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM users WHERE id_user = ?")) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, role = ? WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getRole());
            stmt.setInt(4, user.getIdUser());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(int userId, String newPassword) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE users SET password = ? WHERE id_user = ?")) {
            stmt.setString(1, newPassword); //  cifrar
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
