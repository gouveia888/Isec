package dao;

import database.DatabaseManager;
import database.QueryLogger;

import java.sql.*;

/**
 * BaseDAO provides helper methods for executing SQL queries and updates.
 * Ensures write operations are synchronized using DatabaseManager's lock.
 */
public abstract class BaseDAO {

    protected final Connection connection;
    protected final DatabaseManager dbManager;

    public BaseDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        this.connection = dbManager.getConnection();
    }

    /**
     * Thread-safe execution of an update (INSERT, UPDATE, DELETE, CREATE, etc.)
     */
    protected void executeWrite(String sql, Object... params) throws SQLException {
        synchronized (dbManager.getDbLock()) {
            try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
                setParameters(stmt, params);
                stmt.executeUpdate();
            }
            long version = dbManager.incrementVersion();
            String finalSql = buildSqlForLogging(sql, params);
            QueryLogger.log(version, finalSql);
        }
    }

    protected int executeInsertAndReturnKey(String sql, Object... params) throws SQLException {
        synchronized (dbManager.getDbLock()) {
            try (PreparedStatement stmt =
                         dbManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                setParameters(stmt, params);
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    long version = dbManager.incrementVersion();
                    QueryLogger.log(version, buildSqlForLogging(sql, params));
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Insert failed: no generated key.");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Execute a query (SELECT) - no synchronization needed for reading.
     */
    protected ResultSet executeQuery(String sql, Object... params) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(sql);
        setParameters(stmt, params);
        return stmt.executeQuery();
    }

    /**
     * Helper method to set parameters in a PreparedStatement
     */
    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }

    /**
     * Helper method to convert parameters to an SQL String
     */
    private String buildSqlForLogging(String sqlTemplate, Object... params) {
        String finalSql = sqlTemplate;
        for (Object param : params) {
            String value;

            if (param == null) value = "NULL";
            else value = "'" + param.toString().replace("'", "''") + "'";

            finalSql = finalSql.replaceFirst("\\?", value);
        }
        return finalSql;
    }

}