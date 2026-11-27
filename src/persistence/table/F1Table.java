package persistence.table;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * CreatesTable
 * interface for classes that create tables
 * ensures/maintains shared functionality
 */
public interface F1Table {

    /**
     * Creates the corresponding SQL server table
     * 
     * @throws SQLException on any sql exception while creating the table
     */
    void createTable() throws SQLException;

    /**
     * Inserts data into corresponding table from its csv file
     * 
     * @throws SQLException on invalid data entry
     */
    void insertData() throws SQLException;

    /**
     * Drop table
     * 
     * @throws SQLException on errors while dropping tables
     */
    void dropTable() throws SQLException;

    /**
     * performs SELECT * FROM table_name
     * 
     * @throws SQLException on errors while selecting all from a table
     */
    ResultSet selectAll() throws SQLException;

    /**
     * Getter for the table name
     * 
     * @return the name of the table
     */
    String getTableName();
}
