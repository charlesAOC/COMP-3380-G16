package persistence.table;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;
import logic.Pagination;
import logic.TableName;
import logic.util.Constants;
import logic.util.Strings;
import logic.util.TimeConverter;
import persistence.DropTable;
import persistence.SelectAll;
import ui.Printer;
import ui.util.TabData;

/**
 * ResultTable
 * class to manage the result table
 */
public class ResultTable implements F1Table {

    private final String tableName = TableName.RESULT.getTableName();

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#createTable()
     */
    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session
        String raceTable = TableName.RACE.getTableName();
        String driverTable = TableName.DRIVER.getTableName();
        String constructorTable = TableName.CONSTRUCTOR.getTableName();
        String statusTable = TableName.STATUS.getTableName();

        // create statement for result table
        String str = """
                IF OBJECT_ID(N'%1$s.%2$s', N'U') IS NULL
                BEGIN
                    CREATE TABLE %1$s.%2$s (
                        resultId        INT  PRIMARY KEY NOT NULL,
                        raceId          INT  FOREIGN KEY REFERENCES %1$s.%3$s (raceId) NOT NULL,
                        driverId        INT  FOREIGN KEY REFERENCES %1$s.%4$s (driverId) NOT NULL,
                        constructorId   INT  FOREIGN KEY REFERENCES %1$s.%5$s (constructorId) NOT NULL,
                        number          INT ,
                        grid            INT  NOT NULL,
                        position        INT ,
                        positionOrder   INT  NOT NULL,
                        points          FLOAT ,
                        laps            INT  NOT NULL,
                        milliseconds    INT ,
                        fastestLap      INT ,
                        rank            INT ,
                        fastestLapTime  INT,
                        fastestLapSpeed FLOAT ,
                        statusId        INT  FOREIGN KEY REFERENCES %1$s.%6$s (statusId) NOT NULL
                    );
                END
                """.formatted(username, tableName, raceTable, driverTable, constructorTable, statusTable);

        // create the statement then execute it
        Statement stmt = DBConnection.GetConnection().createStatement();
        stmt.execute(str);
    }

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#insertData()
     */
    @Override
    public void insertData() throws SQLException {

        // insert query
        String str = """
                INSERT  INTO %s.%s (
                    resultId,
                    raceId,
                    driverId,
                    constructorId,
                    number,
                    grid,
                    position,
                    positionOrder,
                    points,
                    laps,
                    milliseconds,
                    fastestLap,
                    rank,
                    fastestLapTime,
                    fastestLapSpeed,
                    statusId
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                        """.formatted(AccountHelper.getUsername(), tableName);
        ;

        // prepared statement to be executed
        Connection connection = DBConnection.GetConnection();
        PreparedStatement stmt = connection.prepareStatement(str);

        // begin transaction
        connection.setAutoCommit(false);

        try {
            // initialize reader
            BufferedReader reader = Files.newBufferedReader(Path.of(Constants.DATASET_PATH + "results.csv"),
                    StandardCharsets.UTF_8);

            // read header row
            String line = reader.readLine();
            int count = 0; // for batching

            // loop through the csv file
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");

                stmt.setInt(1, Integer.parseInt(columns[0]));
                stmt.setInt(2, Integer.parseInt(columns[1]));
                stmt.setInt(3, Integer.parseInt(columns[2]));
                stmt.setInt(4, Integer.parseInt(columns[3]));
                if (Strings.IsValidCell(columns[4]))
                    stmt.setInt(5, Integer.parseInt(columns[4])); ////////////
                stmt.setInt(6, Integer.parseInt(columns[5]));
                if (Strings.IsValidCell(columns[6]))
                    stmt.setInt(7, Integer.parseInt(columns[6]));
                stmt.setInt(8, Integer.parseInt(columns[7]));
                if (Strings.IsValidCell(columns[8]))
                    stmt.setFloat(9, Float.parseFloat(columns[8]));/////////
                stmt.setInt(10, Integer.parseInt(columns[9]));
                if (Strings.IsValidCell(columns[10]))
                    stmt.setInt(11, Integer.parseInt(columns[10]));
                if (Strings.IsValidCell(columns[11]))
                    stmt.setInt(12, Integer.parseInt(columns[11]));
                if (Strings.IsValidCell(columns[12]))
                    stmt.setInt(13, Integer.parseInt(columns[12]));
                if (Strings.IsValidCell(columns[13]))
                    stmt.setInt(14, TimeConverter.ConvertToMilliSec(columns[13]));
                if (Strings.IsValidCell(columns[14]))
                    stmt.setFloat(15, Float.parseFloat(columns[14]));
                stmt.setInt(16, Integer.parseInt(columns[15]));
                stmt.addBatch(); // add to batch

                // add batch to db
                if (++count % Constants.BATCH_SIZE_XL == 0) {
                    stmt.executeBatch();
                }
            }

            // add any left overs
            stmt.executeBatch();

            // close reader
            reader.close();

            // commit the transaction if no error occured (end transaction)
            connection.commit();
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());

            // roll back if any error occurs
            connection.rollback();
        } finally {

            // reset auto commit
            connection.setAutoCommit(true);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#dropTable()
     */
    @Override
    public void dropTable() throws SQLException {
        DropTable.DropATable(tableName);
    }

    // getter
    public String getTableName() {
        return this.tableName;
    }

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#selectAll()
     */
    @Override
    public void selectAll() throws SQLException {

        // perform selct * from table_name query
        ResultSet res = SelectAll.SelectAllFromTable(tableName);

        // output configuration
        TabData[] colData = {
                new TabData("resultId", 10),
                new TabData("raceId", 10),
                new TabData("driverId", 10),
                new TabData("constructorId", 15),
                new TabData("number", 8),
                new TabData("grid", 12),
                new TabData("position", 10),
                new TabData("positionOrder", 15),
                new TabData("points", 10),
                new TabData("laps", 10),
                new TabData("milliseconds", 15),
                new TabData("fastestLap", 15),
                new TabData("rank", 10),
                new TabData("fastestLapTime", 15),
                new TabData("fastestLapSpeed", 15),
                new TabData("statusId", 10),
        };

        // print output header
        int tableWidth = Printer.PrintHeader(colData);

        // init pagination
        Pagination pagination = new Pagination(null);

        // display results
        while (res.next() && !pagination.isTerminated()) {

            // convert output to string
            String[] colValues = { res.getInt(1) + "", // resultId
                    res.getInt(2) + "", // raceId
                    res.getInt(3) + "", // driverId
                    res.getInt(4) + "", // constructorId
                    res.getInt(5) + "", // number
                    res.getInt(6) + "", // grid
                    res.getInt(7) + "", // position
                    res.getInt(8) + "", // positionOrder
                    res.getFloat(9) + "", // points
                    res.getInt(10) + "", // laps
                    res.getInt(11) + "", // milliseconds
                    res.getInt(12) + "", // fastestLap
                    res.getInt(13) + "", // rank
                    res.getInt(14) + "", // fastestLapTime
                    res.getFloat(15) + "", // fastestLapSpeed
                    res.getInt(16) + "" // statusId
            };

            // print output row
            Printer.PrintRow(colData, colValues);

            // trigger pagination at page limit
            pagination.paginate(colData, tableWidth);
        }

        // print output footer
        Printer.PrintFooter(tableWidth);
    }
}
