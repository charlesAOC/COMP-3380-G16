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

/**
 * StatusTable
 * class to handle creating and deleting the status table
 */
public class StatusTable implements F1Table {

    private final String tableName = TableName.STATUS.getTableName();

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#CreateTable()
     */
    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername();

        // create statement
        String str = """
                        IF OBJECT_ID(N'%1$s.%2$s', N'U') IS NULL
                        BEGIN
                            CREATE TABLE %1$s.%2$s (
                                statusId INT          PRIMARY KEY NOT NULL,
                                status   VARCHAR (%3$d) NOT NULL UNIQUE
                            );
                        END
                """.formatted(username, tableName, Constants.MAX_COLUMN_LENGTH);
        ;

        // get db connection
        Connection connection = DBConnection.GetConnection();

        // create and execute statement
        Statement stm = connection.createStatement();
        stm.execute(str);

    }

    /**
     * (non-Javadoc)
     * 
     * @throws IOException
     * 
     * @see persistence.table.F1Table#insertData()
     */
    @Override
    public void insertData() throws SQLException {

        // insert query
        String str = """
                INSERT  INTO %s.%s (
                    statusId,
                    status
                )
                VALUES (?, ?);
                        """.formatted(AccountHelper.getUsername(), tableName);
        ;

        // prepared statement to be executed
        Connection connection = DBConnection.GetConnection();
        PreparedStatement stmt = connection.prepareStatement(str);

        // begin transaction
        connection.setAutoCommit(false);

        try {
            // initialize reader
            BufferedReader reader = Files.newBufferedReader(Path.of(Constants.DATASET_PATH + "status.csv"),
                    StandardCharsets.UTF_8);

            // read header row
            String line = reader.readLine();

            // loop through the csv file
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                stmt.setInt(1, Integer.parseInt(columns[0]));
                stmt.setString(2, columns[1]);
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
        DropTable.DropATable(TableName.STATUS);
    }

    // getter
    public String getTableName() {
        return this.tableName;
    }
}
