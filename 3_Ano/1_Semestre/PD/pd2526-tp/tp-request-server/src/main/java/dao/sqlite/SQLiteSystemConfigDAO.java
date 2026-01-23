package dao.sqlite;


import dao.BaseDAO;
import dao.interfaces.SystemConfigDAO;
import database.DatabaseManager;

import java.sql.*;

public class SQLiteSystemConfigDAO
        extends BaseDAO
        implements SystemConfigDAO {

    public SQLiteSystemConfigDAO(DatabaseManager dbManager) {
        super(dbManager);
    }

    // --------------------------
    // Database version
    // --------------------------

    @Override
    public int getDatabaseVersion() {
        try {
            String v = get("db_version");
            return (v == null) ? 0 : Integer.parseInt(v);
        } catch (Exception _){
            return -1;
        }
    }

    @Override
    public int incrementDatabaseVersion() {
        try {
            int next = getDatabaseVersion() + 1;
            set("db_version", String.valueOf(next));
            return next;
        } catch (Exception _){
            return -1;
        }
    }

    @Override
    public boolean setDatabaseVersion(int version) {
        try {
            set("db_version", String.valueOf(version));
            return true;
        } catch (Exception _){
            return false;
        }
    }

    // --------------------------
    // Instructor code hash
    // --------------------------

    @Override
    public String getInstructorCodeHash() {
        try {
            return get("instructor_code_hash");
        } catch (Exception _){
            return null;
        }
    }

    @Override
    public boolean setInstructorCodeHash(String hash) {
        try {
            set("instructor_code_hash", hash);
            return true;
        } catch (Exception _){
            return false;
        }
    }

    // --------------------------
    // Generic code
    // --------------------------
    private String get(String key) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT value FROM system_config WHERE key = ?")) {

            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("value") : null;
        }
    }

    private void set(String key, String value) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO system_config (key, value) VALUES (?, ?) " +
                "ON CONFLICT(key) DO UPDATE SET value = excluded.value")) {

            stmt.setString(1, key);
            stmt.setString(2, value);
            stmt.executeUpdate();
        }
    }
}
