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
import logic.util.Constants;

/**
 * PitStopTable
 * class to manage the pit stops table
 */
public class PitStopTable implements F1Table {

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#createTable()
     */
    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session

        // create statement for pit stops table
        String str = """
                IF OBJECT_ID(N'%s.pit_stop', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.pit_stop (
                        raceId       INT FOREIGN KEY REFERENCES %<s.race (raceId) NOT NULL,
                        driverId     INT FOREIGN KEY REFERENCES %<s.driver (driverId) NOT NULL,
                        stop         INT NOT NULL,
                        lap          INT NOT NULL,
                        milliseconds INT NOT NULL,
                        PRIMARY KEY (raceId, driverId, stop)
                    );
                END

                """.formatted(username);

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
                INSERT  INTO %s.pit_stop (
                    raceId,
                    driverId,
                    stop,
                    lap,
                    milliseconds
                )
                VALUES (?, ?, ?, ?, ?);
                        """.formatted(AccountHelper.getUsername());
        ;

        // prepared statement to be executed
        Connection connection = DBConnection.GetConnection();
        PreparedStatement stmt = connection.prepareStatement(str);

        // begin transaction
        connection.setAutoCommit(false);

        try {
            // initialize reader
            BufferedReader reader = Files.newBufferedReader(Path.of(Constants.DATASET_PATH + "pit_stops.csv"),
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dropTable'");
    }
}
