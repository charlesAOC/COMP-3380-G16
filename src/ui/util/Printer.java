package ui.util;

public class Printer {

    /**
     * Pirnt a query output row in a tabular format
     * 
     * @param colData   column data for the row that is to be printed [header,
     *                  length]
     * @param colValues values that are to be printed
     */
    public static void PrintRow(TabData[] colData, String[] colValues) {

        // format for output
        String format = "";

        // build up format based on col widths
        for (TabData t : colData) {
            format += "|%" + t.getMaxLenght() + "s";
        }
        format += "|"; // close the table

        System.out.println(String.format(format, (Object[]) colValues));
    }
}
