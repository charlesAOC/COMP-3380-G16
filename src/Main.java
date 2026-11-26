import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import logic.AccountHelper;
import logic.DBConnection;
import persistence.table.CircuitTable;
import persistence.table.ConstructorResultTable;
import persistence.table.ConstructorTable;
import persistence.table.DriverTable;
import persistence.table.RaceTable;
import persistence.table.StatusTable;

/**
 * Main activity for the application
 */
public class Main {

    /**
     * main method for the app *
     * 
     * @param args arguments passed from the command line
     */
    public static void main(String[] args) {

        Properties prop = new Properties();
        String fileName = "auth.cfg";
        try {
            FileInputStream configFile = new FileInputStream(fileName);
            prop.load(configFile);
            configFile.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Could not find config file.");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Error reading config file.");
            System.exit(1);
        }
        String username = (prop.getProperty("username"));
        String password = (prop.getProperty("password"));

        if (username == null || password == null) {
            System.out.println("Username or password not provided.");
            System.exit(1);
        }

        String connectionUrl = "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                + "database=cs3380;"
                + "user=" + username + ";"
                + "password=" + password + ";"
                + "encrypt=false;"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";

        try {
            // set username
            AccountHelper.setUsername(username);

            // initialize the connection
            Connection connection = DBConnection.InitConnection(connectionUrl);

            ConstructorTable.CreateTable();
            CircuitTable.CreateTable();
            StatusTable.CreateTable();
            DriverTable.CreateTable();
            RaceTable.CreateTable();
            ConstructorResultTable.CreateTable();

            // close the connection
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // end of process prompt
        System.out.println("End of Process...");
    }
}