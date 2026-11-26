package persistence.table;

import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;
import logic.util.Constants;

/**
 * RaceTable
 * cclass for managing the race table
 */
public class RaceTable implements F1Table {

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#createTable()
     */
    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session

        // create statement for race table
        String str = """
                IF OBJECT_ID(N'%s.race', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.race (
                        raceId    INT          PRIMARY KEY NOT NULL,
                        year      INT          NOT NULL,
                        round     INT          NOT NULL,
                        circuitId INT          FOREIGN KEY REFERENCES %<s.circuit (circuitId) NOT NULL,
                        name      VARCHAR (%d) NOT NULL,
                        date      DATE         NOT NULL,
                        time      TIME         NOT NULL
                    );
                END
                """.formatted(username, Constants.MAX_COLUMN_LENGTH);

        // create the statement then execute it
        Statement stmt = DBConnection.GetConnection().createStatement();
        stmt.execute(str);
    }

    /**
     * staic method for creating the race table
     * 
     * @throws SQLException
     */
    public static void CreateTable() throws SQLException {
        new RaceTable().createTable();
    }

}
