package logic;

/**
 * Prompter
 * Utility class to prompt the user as well as keep track of the elapsed times
 * of operations.
 * 
 * HOW TO USE-
 * Create a new instance (this is the start point)
 * Call endPrompt() to mark that as the end point.
 * both take a message that is displayed to the user
 */
public class Prompter {

    // instance variables
    private long startTime; // start time of an operaion

    /**
     * Constructor.
     * Prompt user before performing an operation
     * 
     * @param msg to print to sys out
     */
    public Prompter(String msg) {
        this.startTime = System.nanoTime();
        System.out.println(msg);
    }

    /**
     * Prompt the user when the operation is complete
     * Display elapsed time
     * 
     * @param msg to print ot sys out
     */
    public void endPrompt(String msg) {
        long elapsedTime = System.nanoTime() - this.startTime;
        System.out.println(String.format("%s took %d ms", msg, (int) (elapsedTime / 1000000)));
    }

    /**
     * clears the console
     */
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Re-usable prompt class TODO no sure this is actually usefull
     * 
     * @param msg to print to sys out
     */
    public static void PromptUser(String msg) {
        System.out.println(msg);
    }
}
