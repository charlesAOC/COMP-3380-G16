package persistence;

import logic.AccountHelper;

/**
 * Query
 * abstract class to serve as the parent class for all querries
 */
public abstract class Query {

    /**
     * method called to execute query
     */
    public abstract void executeQuery();

    /**
     * @param tableName name of the table being queried
     * @return string respresenting the SQL server table name
     */
    protected String table(String tableName) {
        return AccountHelper.getUsername() + "." + tableName;
    }
}
