package persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;

/**
 * SelectAll
 * Utility class to make SELECT * FROM table_name re-usable
 */
public class SelectAll {

    /**
     * Re-usable function to perform the SELECT * FROM table_name query.
     * returns a ResultSet that can be looped through
     * 
     * @param tableName name of table to query
     * @return SELECT * FROM tableName as a ResultSet
     * @throws SQLException
     */
    public static ResultSet SelectAllFromTable(String tableName) throws SQLException {
        String userName = AccountHelper.getUsername();

        // select statement
        String str = "SELECT * FROM %s.%s;".formatted(userName, tableName);

        // get connection and create statement
        Connection connection = DBConnection.GetConnection();
        Statement statement = connection.createStatement();

        // execute query, return results
        return statement.executeQuery(str);
    }
}
