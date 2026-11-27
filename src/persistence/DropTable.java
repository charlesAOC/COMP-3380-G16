package persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;

/**
 * DropTable
 * Re-usable class for dropping tables
 */
public class DropTable {

    /**
     * Drops a table with a given table name
     * 
     * @param tableName name of table to drop
     * @throws SQLException on error while dropping tables
     */
    public static void DropATable(String tableName) throws SQLException {

        String username = AccountHelper.getUsername();

        // sql statement
        String str = "DROP TABLE IF EXISTS %s.$s;".formatted(username, tableName);

        // create connection
        Connection connection = DBConnection.GetConnection();

        // execute query
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(str);
    }
}
