package persistence.table;

import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;

/**
 * ConstructorStandingTable
 * class to manage the constructor standings table
 */
public class ConstructorStandingTable implements F1Table {

    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session

        // create statement for constructor standings table
        String str = """
                IF OBJECT_ID(N'%s.constructor_standing', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.constructor_standing (
                        constructorStandingsId INT PRIMARY KEY NOT NULL,
                        raceId                 INT FOREIGN KEY REFERENCES %<s.race (raceId) NOT NULL,
                        constructorId          INT FOREIGN KEY REFERENCES %<s.constructor (constructorId) NOT NULL,
                        points                 INT NOT NULL,
                        position               INT NOT NULL,
                        wins                   INT NOT NULL
                    );
                END
                """.formatted(username);

        // create the statement then execute it
        Statement stmt = DBConnection.GetConnection().createStatement();
        stmt.execute(str);
    }

    /**
     * staic method for creating the constructor standings table
     * 
     * @throws SQLException
     */
    public static void CreateTable() throws SQLException {
        new ConstructorStandingTable().createTable();
    }

}
