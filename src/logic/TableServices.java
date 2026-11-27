package logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import persistence.table.CircuitTable;
import persistence.table.ConstructorResultTable;
import persistence.table.ConstructorStandingTable;
import persistence.table.ConstructorTable;
import persistence.table.DriverStandingTable;
import persistence.table.DriverTable;
import persistence.table.F1Table;
import persistence.table.LapTimeTable;
import persistence.table.PitStopTable;
import persistence.table.QualifyingTable;
import persistence.table.RaceTable;
import persistence.table.ResultTable;
import persistence.table.SprintResultTable;
import persistence.table.StatusTable;

/**
 * TableServices
 * Class that acts as a relay between the database and the UI/main process
 */
public class TableServices {

    /**
     * Holds all the tables in their creation/drop/insertion order
     * ORDERING IS IMPORTANT
     * Create/drop/insert would use this list when creating, droping and inserting
     * into tables
     */
    private static final F1Table[] tables = {
            new ConstructorTable(),
            new CircuitTable(),
            new StatusTable(),
            new DriverTable(),
            new RaceTable(),
            new ConstructorResultTable(),
            new ConstructorStandingTable(),
            new DriverStandingTable(),
            new LapTimeTable(),
            new PitStopTable(),
            new QualifyingTable(),
            new SprintResultTable(),
            new ResultTable(),
    };

    /**
     * Creates all the tables related to the project
     * 
     * @throws SQLException on any sql exception while creating tables
     */
    public static void CreateTables() throws SQLException {

        // get connection and begin transaction
        Connection connection = DBConnection.GetConnection();
        connection.setAutoCommit(false);

        try {

            // create tables
            for (F1Table table : tables) {

                // init promt
                Prompter prompt = new Prompter("Creating " + table.getTableName() + " table.");

                // create table
                table.createTable();

                // success promt
                prompt.endPrompt("Successfully created the " + table.getTableName() + " table.");
            }

            // end transaction
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            // incase of error
            connection.rollback();
        } finally {

            // reset auto commit
            connection.setAutoCommit(true);
        }

    }

    /**
     * Inserts all the data from the csv files into the created tables
     * 
     * NOTE - transactions on each table creation as at now
     * 
     * @throws SQLException on any sql exception while inserting data
     */
    public static void InsertData() throws SQLException {
        for (F1Table table : tables) {
            // init promt
            Prompter prompt = new Prompter("Inserting into " + table.getTableName() + " table.");

            // insert into table
            table.insertData();

            // success promt
            prompt.endPrompt("Successfully inserted into the " + table.getTableName() + " table.");
        }
    }

    /**
     * Drop all tables
     * 
     * @throws SQLException on any sql execption while dropping tables
     */
    public static void DropTables() throws SQLException {

        // get connection and begin transaction
        Connection connection = DBConnection.GetConnection();
        connection.setAutoCommit(false);

        try {
            // drop tables in reverse order
            for (int ind = tables.length - 1; ind >= 0; ind--) {
                // init promt
                Prompter prompt = new Prompter("Dropping " + tables[ind].getTableName() + " table.");

                // drop table
                tables[ind].dropTable();

                // success promt
                prompt.endPrompt("Successfully dropped the " + tables[ind].getTableName() + " table.");
            }

            // commit if no errors
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            // roll back if error
            connection.rollback();
        } finally {

            // reset auto commit
            connection.setAutoCommit(true);
        }

    }

    /**
     * performs SELECT * FROM table_name for a table
     * 
     * @param table table to select all from
     * @throws SQLException on errors while selecting all
     */
    public void SelectAllFrom(TableName table) throws SQLException {
        boolean found = false;
        int count = 0;
        String tableName = table.getTableName();

        // loop through the tables
        while (!found && count < tables.length) {

            if (tableName.equals(tables[count].getTableName())) {
                // toggle found
                found = true;

                try {
                    // init promt
                    Prompter prompt = new Prompter("SELECTING * FROM " + tables[count].getTableName() + " table.");

                    // perform SELECT * FROM table_name query
                    ResultSet res = tables[count].selectAll();

                    // success promt
                    prompt.endPrompt("Successfully SELECTED ALL from " + tables[count].getTableName() + " table.");

                } catch (SQLException e) {

                    // failure prompt
                    System.out.println(e.getMessage());
                }
            }

            ++count;
        }
    }
}
