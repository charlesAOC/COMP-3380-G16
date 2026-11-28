package logic;

import java.util.Scanner;

import ui.Printer;
import ui.util.TabData;

/**
 * Pagination
 * utility class to handle query pagination
 */
public class Pagination {

    // default page size if null is passed to constructor
    private static final int DEFAULT_PAGE_SIZE = 100;

    private final int PAGE_SIZE;// configurable max page size
    private boolean isTerminated; // true if user terminates the query result
    private int count;// number of rows printed in current page

    /**
     * constructor to initialize pagination
     * 
     * @param pageSize desired page size | null for default size
     */
    public Pagination(Integer pageSize) {
        this.PAGE_SIZE = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;
        this.isTerminated = false;
        this.count = 0;
    }

    /**
     * Method to trigger pagination behaviour.
     * called inside the while loop. look at the ConstructorResultTable.java for an
     * example on how to use this
     * must be paired with instance.isTerminated() to cancel out of pagination
     * 
     * @param colData    column data for printing headers
     * @param tableWidth column width (all columns)
     */
    public void paginate(TabData[] colData, int tableWidth) {

        // check if its at page limit
        if (this.isPageEnd()) {
            // print table footer
            Printer.PrintFooter(tableWidth);

            // wait for the users decision
            this.nextPageOrExit();

            // if the user continues to the next page
            if (!isTerminated) {
                Printer.PrintHeader(colData);
            }
        }

    }

    /**
     * getter for isTerminated
     * 
     * @return false if the querying is still printing, true terminates
     */
    public boolean isTerminated() {
        return isTerminated;
    }

    /**
     * check if the query printing is at the page limit
     * 
     * @return true if at page limit | false otherwise
     */
    private boolean isPageEnd() {
        ++this.count;

        // true if not a multiple of page size
        return this.count % this.PAGE_SIZE == 0;
    }

    /**
     * helper method to control paginating with user input.
     * if the user enter 'd' print the next page
     * if the user enter 'a' stop the process
     */
    private void nextPageOrExit() {

        try {
            // fetch same scanner as the one running in the UI
            Scanner scanner = ScannerHelper.GetScanner();

            // prompt for user
            System.out.print("Enter 'A' to quit | Enter 'D' to continue: ");

            // read the line
            String input = scanner.nextLine().trim();

            // keep prompting the user for invalid entries
            while (input.equals("a") && input.equals("d")) {
                System.out.print("Enter 'A' to quit | Enter 'D' to continue: ");
                input = scanner.nextLine().trim();// read line
            }

            if (input.equals("a")) {
                this.isTerminated = true; // terminate query loop
            }

        } catch (NullPointerException e) {
            System.out.println(e.getMessage()); // exception with io
        }

    }

}
