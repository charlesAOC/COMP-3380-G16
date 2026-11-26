package persistence.table;

import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;

/**
 * SprintResultTable
 * class to maintain the sprint results table
 */
public class SprintResultTable implements F1Table {

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#createTable()
     */
    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session

        // create statement for sprint results table
        String str = """
                IF OBJECT_ID(N'%s.sprint_result', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.sprint_result (
                        resultId       INT  PRIMARY KEY NOT NULL,
                        raceId         INT  FOREIGN KEY REFERENCES %<s.race (raceId) NOT NULL,
                        driverId       INT  FOREIGN KEY REFERENCES %<s.driver (driverId) NOT NULL,
                        constructorId  INT  FOREIGN KEY REFERENCES %<s.constructor (constructorId) NOT NULL,
                        number         INT  NOT NULL,
                        grid           INT  NOT NULL,
                        position       INT ,
                        positionOrder  INT  NOT NULL,
                        points         INT  NOT NULL,
                        laps           INT  NOT NULL,
                        milliseconds   INT ,
                        fastestLap     INT ,
                        fastestLapTime TIME,
                        statusId       INT  FOREIGN KEY REFERENCES %<s.status (statusId) NOT NULL
                    );
                END
                """.formatted(username);

        // create the statement then execute it
        Statement stmt = DBConnection.GetConnection().createStatement();
        stmt.execute(str);
    }

}
