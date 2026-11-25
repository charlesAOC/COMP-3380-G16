package ui.util.tabulation;

public enum ColDataTypes {
    INT('d')
    , DECIMAL('d')
    , VARCHAR('s');  // maybe make this TEXT

    private final char type;

    ColDataTypes(char type){
        this.type = type;
    }

    // getter
    public char getType(){ return this.type;}
}
