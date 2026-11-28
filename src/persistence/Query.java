package persistence;

import logic.AccountHelper;
import logic.TableName;

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
    protected String table(TableName tableName) {
        return AccountHelper.getUsername() + "." + tableName.getTableName();
    }
}
