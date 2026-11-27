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
 * QualifyingTable
 * class to manage the qualifying table
 */
public class QualifyingTable implements F1Table {

    private final String tableName = TableName.QUALIFYING.getTableName();

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

        // create statement for qualifying table
        String str = """
                IF OBJECT_ID(N'%1$s.%2$s', N'U') IS NULL
                BEGIN
                    CREATE TABLE %1$s.%2$s (
                        qualifyId     INT  PRIMARY KEY NOT NULL,
                        raceId        INT  FOREIGN KEY REFERENCES %1$s.%3$s (raceId) NOT NULL,
                        driverId      INT  FOREIGN KEY REFERENCES %1$s.%4$s (driverId) NOT NULL,
                        constructorId INT  FOREIGN KEY REFERENCES %1$s.%5$s (constructorId) NOT NULL,
                        number        INT  NOT NULL,
                        position      INT  NOT NULL,
                        q1            INT,
                        q2            INT,
                        q3            INT
                    );
                END
                """.formatted(username, tableName, raceTable, driverTable, constructorTable);

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
                    qualifyId,
                    raceId,
                    driverId,
                    constructorId,
                    number,
                    position,
                    q1,
                    q2,
                    q3
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
                        """.formatted(AccountHelper.getUsername(), tableName);
        ;

        // prepared statement to be executed
        Connection connection = DBConnection.GetConnection();
        PreparedStatement stmt = connection.prepareStatement(str);

        // begin transaction
        connection.setAutoCommit(false);
        int count = 0; // for batching

        try {
            // initialize reader
            BufferedReader reader = Files.newBufferedReader(Path.of(Constants.DATASET_PATH + "qualifying.csv"),
                    StandardCharsets.UTF_8);

            // read header row
            String line = reader.readLine();

            // loop through the csv file
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");

                stmt.setInt(1, Integer.parseInt(columns[0]));
                stmt.setInt(2, Integer.parseInt(columns[1]));
                stmt.setInt(3, Integer.parseInt(columns[2]));
                stmt.setInt(4, Integer.parseInt(columns[3]));
                stmt.setInt(5, Integer.parseInt(columns[4]));
                stmt.setInt(6, Integer.parseInt(columns[5]));
                if (Strings.IsValidCell(columns[6]))
                    stmt.setInt(7, TimeConverter.ConvertToMilliSec(columns[6]));
                if (Strings.IsValidCell(columns[7]))
                    stmt.setInt(8, TimeConverter.ConvertToMilliSec(columns[7]));
                if (Strings.IsValidCell(columns[8]))
                    stmt.setInt(9, TimeConverter.ConvertToMilliSec(columns[8]));
                stmt.addBatch(); // add to batch

                // add batch to db
                if (++count % Constants.BATCH_SIZE_REG == 0) {
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
                new TabData("qualifyId", 10),
                new TabData("raceId", 10),
                new TabData("driverId", 8),
                new TabData("constructorId", 8),
                new TabData("number", 13),
                new TabData("position", 13),
                new TabData("q1", 13),
                new TabData("q2", 13),
                new TabData("q3", 13),
        };

        // print output header
        int tableWidth = Printer.PrintHeader(colData);

        // init pagination
        Pagination pagination = new Pagination(null);

        // display results
        while (res.next() && !pagination.isTerminated()) {

            // convert output to string
            String[] colValues = { res.getInt(1) + "",
                    res.getInt(2) + "",
                    res.getInt(3) + "",
                    res.getInt(4) + "",
                    res.getInt(5) + "",
                    res.getInt(6) + "",
                    res.getInt(7) + "",
                    res.getInt(8) + "",
                    res.getInt(9) + "",
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
