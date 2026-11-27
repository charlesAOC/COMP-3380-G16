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
import persistence.DropTable;
import persistence.SelectAll;
import ui.Printer;
import ui.util.TabData;

/**
 * ConstructorResultTable
 * class for managing the constructor result table
 */
public class ConstructorResultTable implements F1Table {

    private final String tableName = TableName.CONSTRUCTOR_RESULT.getTableName();

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#createTable()
     */
    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session
        String raceTable = TableName.RACE.getTableName();
        String constructorTable = TableName.CONSTRUCTOR.getTableName();

        // create statement for constructor result table
        String str = """
                IF OBJECT_ID(N'%1$s.%2$s', N'U') IS NULL
                BEGIN
                    CREATE TABLE %1$s.%2$s (
                        constructorResultsId INT PRIMARY KEY NOT NULL,
                        raceId               INT FOREIGN KEY REFERENCES %1$s.%3$s (raceId) NOT NULL,
                        constructorId        INT FOREIGN KEY REFERENCES %1$s.%4$s (constructorId) NOT NULL,
                        points               DECIMAL NOT NULL
                    );
                END
                """.formatted(username, tableName, raceTable, constructorTable);

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
                    constructorResultsId,
                    raceId,
                    constructorId,
                    points
                )
                VALUES (?, ?, ?, ?);
                        """.formatted(AccountHelper.getUsername(), tableName);
        ;

        // prepared statement to be executed
        Connection connection = DBConnection.GetConnection();
        PreparedStatement stmt = connection.prepareStatement(str);

        // begin transaction
        connection.setAutoCommit(false);

        try {
            // initialize reader
            BufferedReader reader = Files.newBufferedReader(Path.of(Constants.DATASET_PATH + "constructor_results.csv"),
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
                stmt.setFloat(4, Float.parseFloat(columns[3]));
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
     * (non-Javadoc) TODO: implement pagination
     * 
     * @see persistence.table.F1Table#selectAll()
     */
    @Override
    public void selectAll() throws SQLException {

        // perform selct * from table_name query
        ResultSet res = SelectAll.SelectAllFromTable(tableName);

        // output configuration
        TabData[] colData = {
                new TabData("constructorResultsId", 20),
                new TabData("raceId", 10),
                new TabData("constructorId", 15),
                new TabData("points", 10)
        };

        // print output header
        int colWidth = Printer.PrintHeader(colData);

        // initialize pagination
        Pagination pagination = new Pagination(null);

        // display results
        while (res.next() && !pagination.isTerminated()) {

            // convert output to string
            String[] colValues = { res.getInt(1) + "",
                    res.getInt(2) + "",
                    res.getInt(3) + "",
                    res.getFloat(4) + "" };

            // print output row
            Printer.PrintRow(colData, colValues);

            // trigger pagination at page limit
            pagination.paginate();
        }

        // print output footer
        Printer.PrintFooter(colWidth);
    }
}
