package persistence.table;

import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;

/**
 * QualifyingTable
 * class to manage the qualifying table
 */
public class QualifyingTable implements F1Table {

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#createTable()
     */
    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session

        // create statement for qualifying table
        String str = """
                IF OBJECT_ID(N'%s.qualifying', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.qualifying (
                        qualifyId     INT  PRIMARY KEY NOT NULL,
                        raceId        INT  FOREIGN KEY REFERENCES %<s.race (raceId) NOT NULL,
                        driverId      INT  FOREIGN KEY REFERENCES %<s.driver (driverId) NOT NULL,
                        constructorId INT  FOREIGN KEY REFERENCES %<s.constructor (constructorId) NOT NULL,
                        number        INT  NOT NULL,
                        position      INT  NOT NULL,
                        q1            TIME,
                        q2            TIME,
                        q3            TIME
                    );
                END
                """.formatted(username);

        // create the statement then execute it
        Statement stmt = DBConnection.GetConnection().createStatement();
        stmt.execute(str);
    }
}
