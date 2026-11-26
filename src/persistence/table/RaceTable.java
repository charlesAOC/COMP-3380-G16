package persistence.table;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import logic.AccountHelper;
import logic.DBConnection;
import logic.util.Constants;

/**
 * RaceTable
 * cclass for managing the race table
 */
public class RaceTable implements F1Table {

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#createTable()
     */
    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session

        // create statement for race table
        String str = """
                IF OBJECT_ID(N'%s.race', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.race (
                        raceId    INT          PRIMARY KEY NOT NULL,
                        year      INT          NOT NULL,
                        round     INT          NOT NULL,
                        circuitId INT          FOREIGN KEY REFERENCES %<s.circuit (circuitId) NOT NULL,
                        name      VARCHAR (%d) NOT NULL,
                        date      DATE         NOT NULL,
                        time      TIME
                    );
                END
                """.formatted(username, Constants.MAX_COLUMN_LENGTH);

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
                INSERT  INTO %s.race (
                    raceId,
                    year,
                    round,
                    circuitId,
                    name,
                    date,
                    time
                )
                VALUES (?, ?, ?, ?, ?, ?, ?);
                        """.formatted(AccountHelper.getUsername());
        ;

        // prepared statement to be executed
        Connection connection = DBConnection.GetConnection();
        PreparedStatement stmt = connection.prepareStatement(str);

        // begin transaction
        connection.setAutoCommit(false);

        try {
            // initialize reader
            BufferedReader reader = Files.newBufferedReader(Path.of(Constants.DATASET_PATH + "races.csv"),
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
                stmt.setString(5, columns[4]);
                stmt.setDate(6, Date.valueOf(columns[5]));
                if (columns.length == 7)
                    stmt.setTime(7, Time.valueOf(columns[6]));
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
}
