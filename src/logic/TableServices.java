package logic;

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
     * @throws SQLException
     */
    public static void CreateTables() throws SQLException {
        for (F1Table table : tables) {
            table.createTable();
        }
    }
}
