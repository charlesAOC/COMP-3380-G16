package logic;

import java.util.Scanner;

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
     */
    public void paginate() {

        // check if its at page limit
        if (this.isPageEnd()) {
            this.nextPageOrExit();
        }
    }

    /**
     * getter for isTerminated
     * 
     * @return false if the querying is still printing, true terminates
     *         this returning true ends the query
     */
    public boolean isTerminated() {
        return isTerminated;
    }

    /**
     * check if the query printing is at page limit
     * 
     * @return true if at page limit false otherwise
     */
    private boolean isPageEnd() {
        ++this.count;

        // true if not a multiple of page size
        return this.count % this.PAGE_SIZE == 0;
    }

    /**
     * helper method to control paginatin with user input
     */
    private void nextPageOrExit() {

        // initialize scanner
        Scanner scanner = new Scanner(System.in);

        // init scanner for user input
        try {
            boolean inputing = true;

            // input loop (loop through invalid inputs)
            while (inputing) {

                // prompt for user
                System.out.print("Enter 'A' to quit | Enter 'D' to continue: ");
                String input = scanner.nextLine();// read line

                // process input
                if (input.equals("d"))
                    return;
                else if (input.equals("a")) {
                    this.isTerminated = true; // terminate query loop
                    inputing = false; // terminate input loop

                    // close scanner (java is screaming at me)
                    scanner.close();
                }
            }

            // close scanner
            scanner.close();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage()); // exception with io
        }

    }

}
