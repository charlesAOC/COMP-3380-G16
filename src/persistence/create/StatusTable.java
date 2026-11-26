package persistence.create;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;

/**
 * StatusTable
 * class to handle creating and deleting the status table
 */
public class StatusTable implements CreatesTable {

    /**
     * (non-Javadoc)
     * 
     * @see persistence.create.CreatesTable#CreateTable()
     */
    @Override
    public void createTable() throws SQLException {
        // create statement
        String str = """
                        IF OBJECT_ID(N'%s.status', N'U') IS NULL
                        BEGIN
                            CREATE TABLE chuc1.status (
                                statusId INT          PRIMARY KEY NOT NULL,
                                status   VARCHAR (50) CHECK (status IS NOT NULL)
                            );
                        END
                """.formatted(AccountHelper.getUsername());
        ;

        // get db connection
        Connection connection = DBConnection.GetConnection();

        // create and execute statement
        Statement stm = connection.createStatement();
        stm.execute(str);

    }

    /**
     * Factory method to create the status table
     * 
     * @throws SQLException
     */
    public static void CreateTable() throws SQLException {
        new StatusTable().createTable();
    }
}
