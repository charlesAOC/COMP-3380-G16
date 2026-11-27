package logic;

import java.sql.Connection;
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
        for (F1Table table : tables) {
            table.createTable();
        }
    }

    /**
     * Inserts all the data from the csv files into the created tables
     * 
     * @throws SQLException on any sql exception while inserting data
     */
    public static void InsertData() throws SQLException {
        for (F1Table table : tables) {
            table.insertData();
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
                tables[ind].dropTable();
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
}
