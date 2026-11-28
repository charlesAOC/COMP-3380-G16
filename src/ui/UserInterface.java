package ui;

import java.util.Scanner;

import logic.QueryServices;

public class UserInterface {

    public static void runConsole() {

        // init scanner and prompt user
        Scanner console = new Scanner(System.in);

        System.out.println("Welcome! Type h for help.");
        System.out.print("db > ");

        while (console.hasNextLine()) {

            // prepare input
            String line = console.nextLine().trim();
            String[] parts = line.split("\\s+", 2);

            String command = parts[0];
            String arg = parts.length > 1 ? parts[1] : "";

            // end process
            if (command.equals("q")) {
                break;
            }

            // System.out.println("arggg -> ", arg);

            // execute query with args
            boolean cmdExecuted = QueryServices.Execute(command);

            // if command not recognized
            if (!cmdExecuted) {
                System.out.println("Unknown command. Type h for help.");
            }

            // next prompt
            System.out.print("db > ");
        }

        console.close();
        System.out.println("Exiting user interface...");
    }
}
