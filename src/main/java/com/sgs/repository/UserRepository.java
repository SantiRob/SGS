package com.sgs.repository;

import com.sgs.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final Connection connection = DatabaseConnection.getConnection();

    public User findBySapUserAndPassword(String sapUser, String password) {
        String sql = "SELECT id_user, name, sap_user, role, password FROM users WHERE sap_user = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sapUser);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setIdUser(rs.getInt("id_user"));
                user.setName(rs.getString("name"));
                user.setSapUser(rs.getString("sap_user"));
                user.setRole(rs.getString("role"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean sapUserExists(String sapUser) {
        String sql = "SELECT 1 FROM users WHERE sap_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sapUser);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void save(User user) {
        String sql = "INSERT INTO users (name, sap_user, email, role, password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getSapUser());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getPassword());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al registrar usuario:");
            e.printStackTrace();
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT id_user, sap_user, name, email, role, password FROM users")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id_user"),
                        rs.getString("sap_user"),
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
        String sql = "UPDATE users SET name = ?, sap_user = ?, role = ? WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getSapUser());  // ← aquí
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
