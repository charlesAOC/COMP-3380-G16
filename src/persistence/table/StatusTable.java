package persistence.table;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;
import logic.util.Constants;

/**
 * StatusTable
 * class to handle creating and deleting the status table
 */
public class StatusTable implements F1Table {

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
                        IF OBJECT_ID(N'%s.status', N'U') IS NULL
                        BEGIN
                            CREATE TABLE %<s.status (
                                statusId INT          PRIMARY KEY NOT NULL,
                                status   VARCHAR (%d) NOT NULL UNIQUE
                            );
                        END
                """.formatted(username, Constants.MAX_COLUMN_LENGTH);
        ;

        // get db connection
        Connection connection = DBConnection.GetConnection();

        // create and execute statement
        Statement stm = connection.createStatement();
        stm.execute(str);

    }

    @Override
    public void insertData() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertData'");
    }
}
