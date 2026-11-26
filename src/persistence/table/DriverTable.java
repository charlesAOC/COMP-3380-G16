package persistence.table;

import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;
import logic.util.Constants;

/**
 * DriverTable
 * class that manages the driver tables
 */
public class DriverTable implements F1Table {

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
                IF OBJECT_ID(N'%s.driver', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.driver (
                        driverId    INT          PRIMARY KEY NOT NULL,
                        driverRef   VARCHAR (%d) NOT NULL UNIQUE,
                        forename    VARCHAR (%<d) NOT NULL,
                        surname     VARCHAR (%<d) NOT NULL,
                        dob         DATE         NOT NULL,
                        nationality VARCHAR (%<d) NOT NULL
                    );
                END
                """.formatted(username, Constants.MAX_COLUMN_LENGTH);

        // create the statement then execute it
        Statement stmt = DBConnection.GetConnection().createStatement();
        stmt.execute(str);
    }

    /**
     * staic method for creating the driver table
     * 
     * @throws SQLException
     */
    public static void CreateTable() throws SQLException {
        new DriverTable().createTable();
    }

}
