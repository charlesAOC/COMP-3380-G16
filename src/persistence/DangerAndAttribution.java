package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import logic.DBConnection;

public class DangerAndAttribution extends Query {

    @Override
    public void executeQuery() {
        Connection connection = DBConnection.GetConnection();

        String query = "SELECT ci.name AS circuit_name, COUNT(r.resultId) AS total_dnfs " +
                "FROM " + table("result") + " r " +
                "JOIN " + table("race") + " ra ON r.raceId = ra.raceId " +
                "JOIN " + table("circuit") + " ci ON ra.circuitId = ci.circuitId " +
                "JOIN " + table("status") + " s ON r.statusId = s.statusId " +
                "WHERE s.status != 'Finished' AND s.status NOT LIKE '+%' " +
                "GROUP BY ci.circuitId, ci.name " +
                "ORDER BY total_dnfs DESC";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String circuitName = rs.getString("circuit_name");
                int totalDnfs = rs.getInt("total_dnfs");
                System.out.println("Circuit: " + circuitName + ", Total DNFs: " + totalDnfs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
