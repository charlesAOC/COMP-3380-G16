package logic;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import persistence.AvgLapByCircuit;
import persistence.DangerAndAttribution;
import ui.Prompter;

/**
 * QueryServices.
 * Main Utility class that helps with executing defined queries
 */
public class QueryServices {

    // static store for user passed arguments
    private static String USERARG;

    // hash map to organise queries

    /**
     * Map to organise queries
     * Key -> console command
     * Value -> function corresponding to command
     */
    private static final HashMap<String, Runnable> map = new HashMap<>(Map.of(
            "h", () -> printHelp(),
            "Queries", () -> printQueries(),
            "0", () -> (new DangerAndAttribution()).executeQuery(),
            "1", () -> (new AvgLapByCircuit()).executeQuery(),
            "create", () -> CreateTables(),
            "insert", () -> InsertData(),
            "select", () -> SelectData(USERARG),
            "tables", () -> PrintTables(),
            "clear", () -> Prompter.clearConsole()));

    /**
     * method eecute UI commands.
     * The main method that is called by the UI to perform user commands
     * 
     * @param cmd command to run
     * @param arg argument passed with command
     * @return true if command was recognised and executed | false otherwise
     */
    public static boolean Execute(String cmd, String arg) {

        // set static field
        USERARG = arg;

        boolean commandFound = false;

        // check if command exists
        if (map.get(cmd) != null) {

            commandFound = true;

            // run command
            map.get(cmd).run();
        }

        return commandFound;
    }

    // print the help prompt
    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("h              - Display help");
        System.out.println("create         - Create tables");
        System.out.println("insert         - Insert data");
        System.out.println("select <table> - Display table contents");
        System.out.println("tables         - List available tables");
        System.out.println("Queries        -List of available queries");
        System.out.println("clear          -clear the terminal");
        System.out.println("q          - Quit");
    }

    // print the list of queries
    private static void printQueries() {
        System.out.println("Available queries:");
        System.out.println("0              - Danger and Attrition Query");
        System.out.println("1              - Average Lap Time by Circuit Query");
    }

    // create tables
    private static void CreateTables() {
        try {
            TableServices.CreateTables();
            System.out.println("Tables initialized.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // insert data into tables
    private static void InsertData() {
        try {
            TableServices.InsertData();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Select all from a specified table
     * 
     * @param arg table name passed as argument
     */
    private static void SelectData(String arg) {
        if (arg.isEmpty()) {
            System.out.println(
                    "Require a table name.");
        } else {
            TableName table = TableName.valueOf(arg.toUpperCase());

            try {
                TableServices.SelectAllFrom(table);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    // print table names
    private static void PrintTables() {
        for (TableName table : TableName.values()) {
            System.out.println(table);
        }
    }
}
