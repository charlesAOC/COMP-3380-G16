package logic;
import logic.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Queries {
    static Connection connection = DBConnection.GetConnection();


    private String table(String tableName){
        return AccountHelper.getUsername() + "." + tableName;
    }
    
    public void dangerAndAttritionQuery() {
String query =
    "SELECT ci.name AS circuit_name, COUNT(r.resultId) AS total_dnfs " +
    "FROM " + table("result") + " r " +
    "JOIN " + table("race") + " ra ON r.raceId = ra.raceId " +
    "JOIN " + table("circuit") + " ci ON ra.circuitId = ci.circuitId " +
    "JOIN " + table("status") + " s ON r.statusId = s.statusId " +
    "WHERE s.status != 'Finished' AND s.status NOT LIKE '+%' " +
    "GROUP BY ci.circuitId, ci.name " +
    "ORDER BY total_dnfs DESC";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                String circuitName = rs.getString("circuit_name");
                int totalDnfs = rs.getInt("total_dnfs");
                System.out.println("Circuit: " + circuitName + ", Total DNFs: " + totalDnfs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


public void AVGLapByCircuitQuery() {
    String query =
        "SELECT ci.name AS circuit_name, " +
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
