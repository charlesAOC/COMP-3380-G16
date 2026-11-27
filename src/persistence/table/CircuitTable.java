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
 * CircuitTable
 * class to manage the circuits table
 */
public class CircuitTable implements F1Table {

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#createTable()
     */
    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session

        // create statement for circuite table
        String str = """
                IF OBJECT_ID(N'%s.circuit', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.circuit (
                        circuitId  INT          PRIMARY KEY NOT NULL,
                        circuitRef VARCHAR (%d) NOT NULL UNIQUE,
                        name       VARCHAR (%<d) NOT NULL UNIQUE,
                        location   VARCHAR (%<d) NOT NULL,
                        country    VARCHAR (%<d) NOT NULL,
                        lat        DECIMAL          NOT NULL,
                        lng        DECIMAL          NOT NULL,
                        alt        INT          NOT NULL
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
                INSERT  INTO %s.circuit (
                    circuitId,
                    circuitRef,
                    name,
                    location,
                    country,
                    lat,
                    lng,
                    alt
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                        """.formatted(AccountHelper.getUsername());
        ;

        // prepared statement to be executed
        Connection connection = DBConnection.GetConnection();
        PreparedStatement stmt = connection.prepareStatement(str);

        // begin transaction
        connection.setAutoCommit(false);

        try {
            // initialize reader
            BufferedReader reader = Files.newBufferedReader(Path.of(Constants.DATASET_PATH + "circuits.csv"),
                    StandardCharsets.UTF_8);

            // read header row
            String line = reader.readLine();

            // loop through the csv file
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                stmt.setInt(1, Integer.parseInt(columns[0]));
                stmt.setString(2, columns[1]);
                stmt.setString(3, columns[2]);
                stmt.setString(4, columns[3]);
                stmt.setString(5, columns[4]);
                stmt.setDouble(6, Double.valueOf(columns[5]));
                stmt.setDouble(7, Double.valueOf(columns[6]));
                stmt.setInt(8, Integer.valueOf(columns[7]));
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
