package logic.util;

/**
 * Strings
 * utility class for managing strings
 */
public class Strings {

    /**
     * Check if a cell is null or an empty string
     * 
     * @param cell being checked
     * @return true if a cell is not null or empty string
     */
    public static boolean IsValidCell(String cell) {
        return cell != null && cell.trim().length() > 0;
    }
}
