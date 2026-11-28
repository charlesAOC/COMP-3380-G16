package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import logic.DBConnection;

public class AvgLapByCircuit extends Query {

    @Override
    public void executeQuery() {
        Connection connection = DBConnection.GetConnection();

        String query = "SELECT ci.name AS circuit_name, " +
                "AVG(CAST(l.milliseconds AS FLOAT)) AS avg_lap_milliseconds " +
                "FROM " + table("lap_time") + " l " +
                "JOIN " + table("race") + " ra ON l.raceId = ra.raceId " +
                "JOIN " + table("circuit") + " ci ON ra.circuitId = ci.circuitId " +
                "GROUP BY ci.circuitId, ci.name " +
                "ORDER BY avg_lap_milliseconds ASC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String circuitName = rs.getString("circuit_name");
                double avgLapMilliseconds = rs.getDouble("avg_lap_milliseconds");

                System.out.println("Circuit: " + circuitName +
                        ", Average Lap Time: " + avgLapMilliseconds + " ms");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
