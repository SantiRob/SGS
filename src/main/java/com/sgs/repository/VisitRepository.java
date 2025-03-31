package com.sgs.repository;

import com.sgs.model.Visit;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VisitRepository {

    private final Connection connection = DatabaseConnection.getConnection();

    public List<Visit> findAll() {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT id_visit, id_station, id_user, id_maintenance_type, date, result, observations FROM visits";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                visits.add(new Visit(
                        rs.getInt("id_visit"),
                        rs.getInt("id_station"),
                        rs.getInt("id_user"),
                        rs.getInt("id_maintenance_type"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("result"),
                        rs.getString("observations")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return visits;
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM visits WHERE id_visit = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void save(Visit visit) {
        String sql = "INSERT INTO visits (id_station, id_user, id_maintenance_type, date, result, observations) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, visit.getIdStation());
            stmt.setInt(2, visit.getIdUser());
            stmt.setInt(3, visit.getIdMaintenanceType());
            stmt.setDate(4, Date.valueOf(visit.getDate()));
            stmt.setString(5, visit.getResult());
            stmt.setString(6, visit.getObservations());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Visit visit) {
        String sql = "UPDATE visits SET id_station = ?, id_user = ?, id_maintenance_type = ?, date = ?, result = ?, observations = ? WHERE id_visit = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, visit.getIdStation());
            stmt.setInt(2, visit.getIdUser());
            stmt.setInt(3, visit.getIdMaintenanceType());
            stmt.setDate(4, Date.valueOf(visit.getDate()));
            stmt.setString(5, visit.getResult());
            stmt.setString(6, visit.getObservations());
            stmt.setInt(7, visit.getIdVisit());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
