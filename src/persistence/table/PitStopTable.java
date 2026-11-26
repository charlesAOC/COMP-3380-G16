package persistence.table;

import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;

/**
 * PitStopTable
 * class to manage the pit stops table
 */
public class PitStopTable implements F1Table {

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#createTable()
     */
    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session

        // create statement for pit stops table
        String str = """
                IF OBJECT_ID(N'%s.pit_stop', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.pit_stop (
                        raceId       INT FOREIGN KEY REFERENCES %<s.race (raceId) NOT NULL,
                        driverId     INT FOREIGN KEY REFERENCES %<s.driver (driverId) NOT NULL,
                        stop         INT NOT NULL,
                        lap          INT NOT NULL,
                        milliseconds INT NOT NULL,
                        PRIMARY KEY (raceId, driverId, stop)
                    );
                END

                """.formatted(username);

        // create the statement then execute it
        Statement stmt = DBConnection.GetConnection().createStatement();
        stmt.execute(str);
    }
}
