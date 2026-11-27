package ui;
import java.util.Scanner;
import java.sql.SQLException;

import logic.TableServices;
import logic.TableName;

public class UserInterface {

    public static void runConsole() {

        Scanner console = new Scanner(System.in);

        System.out.println("Welcome! Type h for help.");
        System.out.print("db > ");

        while (console.hasNextLine()) {

            String line = console.nextLine().trim();
            String[] parts = line.split("\\s+", 2);

            String command = parts[0];
            String arg = parts.length > 1 ? parts[1] : "";

            if (command.equals("q")) {
                break;
            }

            try {
                switch (command) {

                    case "h":
                        printHelp();
                        break;
                    case "Queries":
                        printQueries();
                        break;


                    case "create":
                        TableServices.CreateTables();
                        System.out.println("Tables initialized.");
                        break;

                    case "insert":
                        TableServices.InsertData();
                        break;

                    case "select":
                        if (arg.isEmpty()) {
                            System.out.println(
                                "Require a table name."
                            );
                        } else {
                            TableName table =
                                TableName.valueOf(arg.toUpperCase());

                            TableServices.SelectAllFrom(table);
                        }
                        break;

                    case "tables":
                        for (TableName table : TableName.values()) {
                            System.out.println(table);
                        }
                        break;

                    case "":
                        break;

                    default:
                        System.out.println(
                            "Unknown command. Type h for help."
                        );
                        break;
                        


                        //TODO: SQL QUERY COMMANDS
                        //TODO: BACKUP DB COMMAND
                        case "0":
                            logic.Queries queries = new logic.Queries();
                            queries.dangerAndAttritionQuery();
                            break;

                        case "1":
                            logic.Queries queries1 = new logic.Queries();
                            queries1.AVGLapByCircuitQuery();
                            break;
                        

                }

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid argument.");

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.print("db > ");
        }

        console.close();
        System.out.println("Exiting user interface...");
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("h              - Display help");
        System.out.println("create         - Create tables");
        System.out.println("insert         - Insert data");
        System.out.println("select <table> - Display table contents");
        System.out.println("tables         - List available tables");
        System.out.println("Queries        -List of available queries");
        System.out.println("q          - Quit");
    }
    private static void printQueries() {
        System.out.println("Available queries:");
        System.out.println("0              - Danger and Attrition Query");
        System.out.println("1              - Average Lap Time by Circuit Query");
    }
}
