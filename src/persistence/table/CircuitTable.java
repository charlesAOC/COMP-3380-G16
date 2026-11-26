package persistence.table;

import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;
import logic.util.Constants;

/**
 * CircuitTable
 * class to manage the circuits table
 */
public class CircuitTable implements F1Table {

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
                IF OBJECT_ID(N'%s.circuit', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.circuit (
                        circuitId  INT          PRIMARY KEY NOT NULL,
                        circuitRef VARCHAR (%d) NOT NULL UNIQUE,
                        name       VARCHAR (%<d) NOT NULL UNIQUE,
                        location   VARCHAR (%<d) NOT NULL,
                        country    VARCHAR (%<d) NOT NULL,
                        lat        INT          NOT NULL,
                        lng        INT          NOT NULL,
                        alt        INT          NOT NULL
                    );
                END
                            """.formatted(username, Constants.MAX_COLUMN_LENGTH);

        // create the statement then execute it
        Statement stmt = DBConnection.GetConnection().createStatement();
        stmt.execute(str);
    }
}
