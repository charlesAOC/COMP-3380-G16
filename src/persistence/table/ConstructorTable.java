package persistence.table;

import java.sql.SQLException;
import java.sql.Statement;

import logic.AccountHelper;
import logic.DBConnection;
import logic.util.Constants;

/**
 * ConstructorTable
 * class that manages the constructor table
 */
public class ConstructorTable implements F1Table {

    @Override
    public void createTable() throws SQLException {
        String username = AccountHelper.getUsername(); // get the username for the session

        // create statement for constructor table
        String str = """
                IF OBJECT_ID(N'%s.constructor', N'U') IS NULL
                BEGIN
                    CREATE TABLE %<s.constructor (
                        constructorId  INT          PRIMARY KEY NOT NULL,
                        constructorRef VARCHAR (%d) NOT NULL UNIQUE,
                        name           VARCHAR (%<d) NOT NULL UNIQUE,
                        nationality    VARCHAR (%<d) NOT NULL
                    );
                END
                """.formatted(username, Constants.MAX_COLUMN_LENGTH);

        // create the statement then execute it
        Statement stmt = DBConnection.GetConnection().createStatement();
        stmt.execute(str);
    }

    /**
     * staic method for creating the constructor table
     * 
     * @throws SQLException
     */
    public static void CreateTable() throws SQLException {
        new ConstructorTable().createTable();
    }

}
