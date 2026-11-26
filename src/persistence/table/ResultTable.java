package persistence.table;

import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;

/**
 * ResultTable
 * class to manage the result table
 */
public class ResultTable implements F1Table {

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#createTable()
     */
    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session

        // create statement for result table
        String str = """
                IF OBJECT_ID(N'%s.result', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.result (
                        resultId        INT  PRIMARY KEY NOT NULL,
                        raceId          INT  FOREIGN KEY REFERENCES %<s.race (raceId) NOT NULL,
                        driverId        INT  FOREIGN KEY REFERENCES %<s.driver (driverId) NOT NULL,
                        constructorId   INT  FOREIGN KEY REFERENCES %<s.constructor (constructorId) NOT NULL,
                        number          INT  NOT NULL,
                        grid            INT  NOT NULL,
                        position        INT ,
                        positionOrder   INT  NOT NULL,
                        points          INT ,
                        laps            INT  NOT NULL,
                        milliseconds    INT ,
                        fastestLap      INT ,
                        rank            INT ,
                        fastestLapTime  TIME,
                        fastestLapSpeed INT ,
                        statusId        INT  FOREIGN KEY REFERENCES %<s.status (statusId) NOT NULL
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
