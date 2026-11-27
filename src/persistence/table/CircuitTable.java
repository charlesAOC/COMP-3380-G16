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
import logic.TableName;
import logic.util.Constants;
import persistence.DropTable;
import persistence.SelectAll;
import ui.util.Printer;
import ui.util.TabData;

/**
 * CircuitTable
 * class to manage the circuits table
 */
public class CircuitTable implements F1Table {

    private final String tableName = TableName.CIRCUIT.getTableName();

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
                IF OBJECT_ID(N'%1$s.%2$s', N'U') IS NULL
                BEGIN
                    CREATE TABLE %1$s.%2$s (
                        circuitId  INT           PRIMARY KEY NOT NULL,
                        circuitRef VARCHAR (%3$d)  NOT NULL UNIQUE,
                        name       VARCHAR (%3$d) NOT NULL UNIQUE,
                        location   VARCHAR (%3$d) NOT NULL,
                        country    VARCHAR (%3$d) NOT NULL,
                        lat        FLOAT          NOT NULL,
                        lng        FLOAT          NOT NULL,
                        alt        INT            NOT NULL
                    );
                END
                            """.formatted(username, tableName, Constants.MAX_COLUMN_LENGTH);

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
                        """.formatted(AccountHelper.getUsername(), tableName);
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
            int count = 0; // for batching

            // loop through the csv file
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                stmt.setInt(1, Integer.parseInt(columns[0]));
                stmt.setString(2, columns[1]);
                stmt.setString(3, columns[2]);
                stmt.setString(4, columns[3]);
                stmt.setString(5, columns[4]);
                stmt.setFloat(6, Float.parseFloat(columns[5]));
                stmt.setFloat(7, Float.parseFloat(columns[6]));
                stmt.setInt(8, Integer.parseInt(columns[7]));
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
                new TabData(15, "circuitId"),
                new TabData(15, "circuitRef"),
                new TabData(40, "name"),
                new TabData(25, "location"),
                new TabData(15, "country"),
                new TabData(15, "lat"),
                new TabData(15, "lng"),
                new TabData(10, "alt")
        };

        // display results
        while (res.next()) {

            // convert output to string
            String[] colValues = { res.getInt(1) + "",
                    res.getString(2),
                    res.getString(3),
                    res.getString(4),
                    res.getString(5),
                    res.getFloat(6) + "",
                    res.getFloat(7) + "",
                    res.getInt(8) + "" };

            // print output
            Printer.PrintRow(colData, colValues);
        }
    }
}
