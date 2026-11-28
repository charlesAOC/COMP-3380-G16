package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import logic.DBConnection;
import logic.TableName;
import ui.Printer;
import ui.util.TabData;

public class DangerAndAttribution extends Query {

    @Override
    public void executeQuery() {
        Connection connection = DBConnection.GetConnection();

        String query = "SELECT ci.name AS circuit_name, COUNT(r.resultId) AS total_dnfs " +
                "FROM " + table(TableName.RESULT) + " r " +
                "JOIN " + table(TableName.RACE) + " ra ON r.raceId = ra.raceId " +
                "JOIN " + table(TableName.CIRCUIT) + " ci ON ra.circuitId = ci.circuitId " +
                "JOIN " + table(TableName.STATUS) + " s ON r.statusId = s.statusId " +
                "WHERE s.status != 'Finished' AND s.status NOT LIKE '+%' " +
                "GROUP BY ci.circuitId, ci.name " +
                "ORDER BY total_dnfs DESC";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            // output configuration
            TabData[] colData = {
                    new TabData("circuit_name", 45),
                    new TabData("total_dnfs", 15)
            };

            // print output header
            int tableWidth = Printer.PrintHeader(colData);

            while (rs.next()) {
                // convert output to string
                String[] colValues = { rs.getString(1), rs.getInt(2) + "" };

                // print output row
                Printer.PrintRow(colData, colValues);
            }

            // print output footer
            Printer.PrintFooter(tableWidth);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
