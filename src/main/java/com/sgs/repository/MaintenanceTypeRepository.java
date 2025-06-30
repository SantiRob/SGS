package com.sgs.repository;

import com.sgs.model.MaintenanceType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceTypeRepository {

    private final Connection connection = DatabaseConnection.getConnection();

    public List<MaintenanceType> findAll() {
        List<MaintenanceType> types = new ArrayList<>();
        String sql = "SELECT id_type, name, description FROM maintenance_types";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                types.add(new MaintenanceType(
                        rs.getInt("id_type"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return types;
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM maintenance_types WHERE id_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void save(MaintenanceType type) {
        String sql = "INSERT INTO maintenance_types (name, description) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type.getName());
            stmt.setString(2, type.getDescription());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(MaintenanceType type) {
        String sql = "UPDATE maintenance_types SET name = ?, description = ? WHERE id_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type.getName());
            stmt.setString(2, type.getDescription());
            stmt.setInt(3, type.getIdType());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> findAllNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT name FROM maintenance_types";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return names;
    }
}
