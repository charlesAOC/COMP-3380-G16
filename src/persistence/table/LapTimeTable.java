package persistence.table;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;
import logic.TableName;
import logic.util.Constants;
import persistence.DropTable;
import persistence.SelectAll;

/**
 * LapTimeTable
 * class to manage lap times table
 */
public class LapTimeTable implements F1Table {

    private final String tableName = TableName.LAP_TIME.getTableName();

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

        // create statement for lap times table
        String str = """
                IF OBJECT_ID(N'%1$s.%2$s', N'U') IS NULL
                BEGIN
                    CREATE TABLE %1$s.%2$s (
                        raceId       INT FOREIGN KEY REFERENCES %1$s.%3$s (raceId) NOT NULL,
                        driverId     INT FOREIGN KEY REFERENCES %1$s.%4$s (driverId) NOT NULL,
                        lap          INT NOT NULL,
                        position     INT NOT NULL,
                        milliseconds INT NOT NULL,
                        PRIMARY KEY (raceId, driverId, lap)
                    );
                END
                """.formatted(username, tableName, raceTable, driverTable);

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
                    raceId,
                    driverId,
                    lap,
                    position,
                    milliseconds

                )
                VALUES (?, ?, ?, ?, ?);
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
            BufferedReader reader = Files.newBufferedReader(Path.of(Constants.DATASET_PATH + "lap_times.csv"),
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
        SelectAll.SelectAllFromTable(tableName);
    }
}
