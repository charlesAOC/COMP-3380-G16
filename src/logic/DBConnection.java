package logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection
 * Singleton class that holds the connection to the database
 * and method to fetch that connection from anywhere in the app
 */
public class DBConnection {

    // connection to database
    private static Connection Connection;

    /**
     * Factory method for initializing DB connection
     * 
     * @param connectionURL connection url
     * @return the Connection obj that was just created
     * @throws SQLException for any sql exections while creating the connection
     */
    public static Connection InitConnection(String connectionURL) throws SQLException {
        Connection = DriverManager.getConnection(connectionURL);
        return Connection;
    }

    // getter for connection
    public static Connection GetConnection() {
        return Connection;
    }
}
