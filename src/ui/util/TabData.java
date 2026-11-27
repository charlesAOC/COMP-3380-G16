package ui.util;

/**
 * TabData.
 * utility class for holding table data
 */
public class TabData {

    // instance methods
    private int maxLenght; // max column width
    private String header; // column header

    /**
     * Constructor.
     * stores column data
     * 
     * @param maxLenght maximum length for the table column
     * @param header    column name
     */
    public TabData(int maxLenght, String header) {
        this.maxLenght = Math.max(maxLenght, header.length());
        this.header = header;
    }

    // getters
    public int getMaxLenght() {
        return maxLenght;
    }

    public String getHeader() {
        return header;
    }
}