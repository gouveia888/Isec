package dao.sqlite;

import dao.BaseDAO;
import database.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQLite implementation of both StudentDAO and InstructorDAO.
 */
public class SQLiteUserDAO
        extends BaseDAO {

    public SQLiteUserDAO(DatabaseManager dbManager) {
        super(dbManager);
    }

    public boolean emailExists(String email) throws SQLException {
        String sqlStudent = "SELECT 1 FROM students WHERE email = ?";
        try (ResultSet rs = executeQuery(sqlStudent, email)) {
            if (rs.next()) return true;
        }

        String sqlInstructor = "SELECT 1 FROM instructors WHERE email = ?";
        try (ResultSet rs = executeQuery(sqlInstructor, email)) {
            if (rs.next()) return true;
        }

        return false;
    }
}