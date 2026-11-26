package persistence.table;

import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;

/**
 * LapTimeTable
 * class to manage lap times table
 */
public class LapTimeTable implements F1Table {

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#createTable()
     */
    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session

        // create statement for lap times table
        String str = """
                IF OBJECT_ID(N'%s.lap_time', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.lap_time (
                        raceId       INT FOREIGN KEY REFERENCES %<s.race (raceId) NOT NULL,
                        driverId     INT FOREIGN KEY REFERENCES %<s.driver (driverId) NOT NULL,
                        lap          INT NOT NULL,
                        position     INT NOT NULL,
                        milliseconds INT NOT NULL,
                        PRIMARY KEY (raceId, driverId, lap)
                    );
                END
                """.formatted(username);

        // create the statement then execute it
        Statement stmt = DBConnection.GetConnection().createStatement();
        stmt.execute(str);
    }

    /**
     * staic method for creating the lap times table
     * 
     * @throws SQLException
     */
    public static void CreateTable() throws SQLException {
        new LapTimeTable().createTable();
    }

}
