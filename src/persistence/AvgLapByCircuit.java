package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import logic.DBConnection;
import logic.TableName;
import ui.Printer;
import ui.util.TabData;

/**
 * AvgLapByCircuit
 * Average lap by circuit.
 */
public class AvgLapByCircuit extends Query {

    @Override
    public void executeQuery() {
        Connection connection = DBConnection.GetConnection();

        String query = "SELECT ci.name AS circuit_name, " +
                "AVG(CAST(l.milliseconds AS FLOAT)) AS avg_lap_milliseconds " +
                "FROM " + table(TableName.LAP_TIME) + " l " +
                "JOIN " + table(TableName.RACE) + " ra ON l.raceId = ra.raceId " +
                "JOIN " + table(TableName.CIRCUIT) + " ci ON ra.circuitId = ci.circuitId " +
                "GROUP BY ci.circuitId, ci.name " +
                "ORDER BY avg_lap_milliseconds ASC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            // output configuration
            TabData[] colData = {
                    new TabData("circuit_name", 45),
                    new TabData("avg_lap_milliseconds", 22)
            };

            // print output header
            int tableWidth = Printer.PrintHeader(colData);

            while (rs.next()) {

                // convert output to string
                String[] colValues = { rs.getString(1), "%.4f ms".formatted(rs.getDouble(2)) };

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
