package persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;
import logic.Pagination;
import logic.TableName;
import ui.Printer;
import ui.util.TabData;

/**
 * FastestPitStopByDriverByYear
 * fastest pit stops times
 * 
 */
public class FastestPitStops extends Query {

    String driver = TableName.DRIVER.getTableName();
    String pitStop = TableName.PIT_STOP.getTableName();
    String race = TableName.RACE.getTableName();

    @Override
    public void executeQuery() {

        // fetch username
        String username = AccountHelper.getUsername();

        String str = """
                SELECT   driver.driverId,
                         driver.forename,
                         driver.surname,
                         race.year,
                         pit.milliseconds
                FROM     %1$s.%2$s AS driver
                         INNER JOIN
                         %1$s.%3$s AS pit
                         ON driver.driverId = pit.driverId
                         INNER JOIN
                         %1$s.%4$s AS race
                         ON race.raceId = pit.raceId
                ORDER BY pit.milliseconds;
                                """.formatted(username, driver, pitStop, race);

        // init connection
        try (Connection connection = DBConnection.GetConnection()) {

            Statement statement = connection.createStatement();

            ResultSet res = statement.executeQuery(str);

            // table config
            TabData[] tableData = {
                    new TabData("driverId", 15),
                    new TabData("forename", 20),
                    new TabData("surname", 20),
                    new TabData("year", 8),
                    new TabData("time (ms)", 10)
            };

            // print output header
            int tableWidth = Printer.PrintHeader(tableData);

            // initialize pagination
            Pagination pagination = new Pagination(null);

            // display results
            while (res.next() && !pagination.isTerminated()) {

                String[] colValues = {
                        res.getInt(1) + "",
                        res.getString(2),
                        res.getString(3),
                        res.getInt(4) + "",
                        res.getInt(5) + "",
                };

                // print output row
                Printer.PrintRow(tableData, colValues);

                // trigger pagination at page limit
                pagination.paginate(tableData, tableWidth);

            }

            // print output footer
            Printer.PrintFooter(tableWidth);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}
