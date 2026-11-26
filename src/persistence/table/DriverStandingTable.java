package persistence.table;

import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;

/**
 * DriverStandingTable
 * class to manage the driver standng table
 */
public class DriverStandingTable implements F1Table {

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#createTable()
     */
    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session

        // create statement for driver standings table
        String str = """
                IF OBJECT_ID(N'%s.driver_standing', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.driver_standing (
                        driverStandingsId INT PRIMARY KEY NOT NULL,
                        raceId            INT FOREIGN KEY REFERENCES %<s.race (raceId) NOT NULL,
                        driverId          INT FOREIGN KEY REFERENCES %<s.driver (driverId) NOT NULL,
                        points            INT NOT NULL,
                        position          INT NOT NULL,
                        wins              INT NOT NULL
                    );
                END
                """.formatted(username);

        // create the statement then execute it
        Statement stmt = DBConnection.GetConnection().createStatement();
        stmt.execute(str);
    }

    @Override
    public void insertData() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertData'");
    }
}
