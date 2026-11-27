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
import logic.util.Strings;
import logic.util.TimeConverter;
import persistence.DropTable;

/**
 * SprintResultTable
 * class to maintain the sprint results table
 */
public class SprintResultTable implements F1Table {

    private final String tableName = TableName.SPRINT_RESULT.getTableName();

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

        // create statement for sprint results table
        String str = """
                IF OBJECT_ID(N'%1$s.%2$s', N'U') IS NULL
                BEGIN
                    CREATE TABLE %1$s.%2$s (
                        resultId       INT  PRIMARY KEY NOT NULL,
                        raceId         INT  FOREIGN KEY REFERENCES %1$s.%3$s (raceId) NOT NULL,
                        driverId       INT  FOREIGN KEY REFERENCES %1$s.%4$s (driverId) NOT NULL,
                        constructorId  INT  FOREIGN KEY REFERENCES %1$s.%5$s (constructorId) NOT NULL,
                        number         INT  NOT NULL,
                        grid           INT  NOT NULL,
                        position       INT ,
                        positionOrder  INT  NOT NULL,
                        points         INT  NOT NULL,
                        laps           INT  NOT NULL,
                        milliseconds   INT ,
                        fastestLap     INT ,
                        fastestLapTime INT,
                        statusId       INT  FOREIGN KEY REFERENCES %1$s.%6$s (statusId) NOT NULL
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
                    fastestLapTime,
                    statusId
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                        """.formatted(AccountHelper.getUsername(), tableName);
        ;

        // prepared statement to be executed
        Connection connection = DBConnection.GetConnection();
        PreparedStatement stmt = connection.prepareStatement(str);

        // begin transaction
        connection.setAutoCommit(false);

        try {
            // initialize reader
            BufferedReader reader = Files.newBufferedReader(Path.of(Constants.DATASET_PATH + "sprint_results.csv"),
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
                    stmt.setInt(7, Integer.parseInt(columns[6]));
                stmt.setInt(8, Integer.parseInt(columns[7]));
                stmt.setInt(9, Integer.parseInt(columns[8]));
                stmt.setInt(10, Integer.parseInt(columns[9]));
                if (Strings.IsValidCell(columns[10]))
                    stmt.setInt(11, Integer.parseInt(columns[10]));
                if (Strings.IsValidCell(columns[11]))
                    stmt.setInt(12, Integer.parseInt(columns[11]));
                if (Strings.IsValidCell(columns[12]))
                    stmt.setInt(13, TimeConverter.ConvertToMilliSec(columns[12]));
                stmt.setInt(14, Integer.parseInt(columns[13]));
                stmt.executeUpdate();
            }

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
        DropTable.DropATable(TableName.SPRINT_RESULT);
    }

    // getter
    public String getTableName() {
        return this.tableName;
    }
}
