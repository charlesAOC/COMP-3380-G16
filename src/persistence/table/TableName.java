package persistence.table;

/**
 * TableName
 * Enum to hold table names. prevents hard coding
 */
public enum TableName {

    // table names
    CONSTRUCTOR("constructor"),
    CIRCUIT("circuit"),
    STATUS("status"),
    DRIVER("driver"),
    RACE("race"),
    CONSTRUCTOR_RESULT("constructor_result"),
    CONSTRUCTOR_STANDING("constructor_standing"),
    DRIVER_STANDING("driver_standing"),
    LAP_TIME("lap_time"),
    PIT_STOP("pit_stop"),
    QUALIFYING("qualifying"),
    SPRINT_RESULT("sprint_result"),
    RESULT("result");

    // instance method for string
    private String tableName;

    // enum constructor
    TableName(String tableName) {
        this.tableName = tableName;
    }

    // getter
    public String getTableName() {
        return this.tableName;
    }
}
