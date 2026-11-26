package persistence.table;

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
}
