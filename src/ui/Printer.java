package ui;

import java.util.Arrays;

import ui.util.TabData;

/**
 * Printer
 * utility method to print query outputs
 */
public class Printer {

    /**
     * prints the header row for a query output
     * 
     * @param colData column data for the output table
     * @return table width for the output table
     */
    public static int PrintHeader(TabData[] colData) {

        // build format
        String format = buidFormat(colData);

        // extract table headers
        Object[] colHeaders = Arrays.stream(colData).map(col -> col.getHeader()).toArray();

        // prepare header row
        String headerRow = String.format(format, colHeaders);

        // print "-" above the table
        System.out.println("-".repeat(headerRow.length()));

        // print output row
        System.out.println(headerRow);

        // print "-" below the header row
        System.out.println("-".repeat(headerRow.length()));

        // return row length
        return headerRow.length();
    }

    /**
     * Pirnt a query output row in a tabular format
     * 
     * @param colData   column data for the row that is to be printed [header,
     *                  length]
     * @param colValues values that are to be printed
     */
    public static void PrintRow(TabData[] colData, String[] colValues) {

        // format for output
        String format = buidFormat(colData);

        // print out table row
        System.out.println(String.format(format, (Object[]) colValues));
    }

    /**
     * Prints a footer for the output table
     * 
     * @param tableWidth width of the output table (col width it passed from
     *                   PrintHeader)
     */
    public static void PrintFooter(int tableWidth) {
        System.out.println("-".repeat(tableWidth));
    }

    /**
     * helper method that build the output format
     * 
     * @param colData column data to build for
     * @return output format (string)
     */
    private static String buidFormat(TabData[] colData) {
        // format for output
        String format = "";

        // build up format based on col widths
        for (TabData t : colData) {
            format += "|%" + t.getMaxLenght() + "s";
        }
        format += "|"; // close the table

        return format;
    }
}
