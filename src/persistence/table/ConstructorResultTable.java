package persistence.table;

import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;

/**
 * ConstructorResultTable
 * class for managing the constructor result table
 */
public class ConstructorResultTable implements F1Table {

    /**
     * (non-Javadoc)
     * 
     * @see persistence.table.F1Table#createTable()
     */
    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session

        // create statement for constructor result table
        String str = """
                IF OBJECT_ID(N'%s.constructor_result', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.constructor_result (
                        constructorResultsId INT PRIMARY KEY NOT NULL,
                        raceId               INT FOREIGN KEY REFERENCES %<s.race (raceId) NOT NULL,
                        constructorId        INT FOREIGN KEY REFERENCES %<s.constructor (constructorId) NOT NULL,
                        points               INT NOT NULL
                    );
                END
                """.formatted(username);

        // create the statement then execute it
        Statement stmt = DBConnection.GetConnection().createStatement();
        stmt.execute(str);
    }
}
