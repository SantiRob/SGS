package com.sgs.repository;

import com.sgs.model.Station;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StationRepository {

    private final Connection connection = DatabaseConnection.getConnection();

    public List<Station> findAll() {
        List<Station> stations = new ArrayList<>();
        String sql = "SELECT id_station, name, address, type, contact_email, status FROM stations";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                stations.add(new Station(
                        rs.getInt("id_station"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("type"),
                        rs.getString("contact_email"),
                        rs.getString("status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stations;
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM stations WHERE id_station = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void save(Station station) {
        String sql = "INSERT INTO stations (name, address, type, contact_email, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, station.getName());
            stmt.setString(2, station.getAddress());
            stmt.setString(3, station.getType());
            stmt.setString(4, station.getContactEmail());
            stmt.setString(5, station.getStatus());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Station station) {
        String sql = "UPDATE stations SET name = ?, address = ?, type = ?, contact_email = ?, status = ? WHERE id_station = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, station.getName());
            stmt.setString(2, station.getAddress());
            stmt.setString(3, station.getType());
            stmt.setString(4, station.getContactEmail());
            stmt.setString(5, station.getStatus());
            stmt.setInt(6, station.getIdStation());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
