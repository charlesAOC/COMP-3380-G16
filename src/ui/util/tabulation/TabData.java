package ui.util.tabulation;

/**
 * TabData. class for holding data about columns for printing tabular data
 */
public class TabData {

    private int maxLenght;
    private ColDataTypes type;
    private int index;

    public TabData(int maxLenght, ColDataTypes type, int index){
        this.maxLenght = maxLenght;
        this.type = type;
        this.index = index;
    }

    //getters
    public int getMaxLenght() {
        return maxLenght;
    }

    public ColDataTypes getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }    
}