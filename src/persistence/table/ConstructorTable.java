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
 * ConstructorTable
 * class that manages the constructor table
 */
public class ConstructorTable implements F1Table {

    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session

        // create statement for constructor table
        String str = """
                IF OBJECT_ID(N'%s.constructor', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.constructor (
                        constructorId  INT          PRIMARY KEY NOT NULL,
                        constructorRef VARCHAR (%d) NOT NULL UNIQUE,
                        name           VARCHAR (%<d) NOT NULL UNIQUE,
                        nationality    VARCHAR (%<d) NOT NULL
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
                INSERT  INTO %s.constructor (
                    constructorId,
                    constructorRef,
                    name,
                    nationality
                )
                VALUES (?, ?, ?, ?);
                        """.formatted(AccountHelper.getUsername());
        ;

        // prepared statement to be executed
        Connection connection = DBConnection.GetConnection();
        PreparedStatement stmt = connection.prepareStatement(str);

        // begin transaction
        connection.setAutoCommit(false);

        try {
            // initialize reader
            BufferedReader reader = Files.newBufferedReader(Path.of(Constants.DATASET_PATH + "constructors.csv"),
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
        DropTable.DropATable(TableName.CONSTRUCTOR);
    }
}
