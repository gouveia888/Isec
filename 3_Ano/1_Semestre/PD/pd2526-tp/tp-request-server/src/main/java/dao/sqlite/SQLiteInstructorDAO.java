package dao.sqlite;

import dao.BaseDAO;
import dao.interfaces.InstructorDAO;
import database.DatabaseManager;
import model.Instructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteInstructorDAO
        extends SQLiteUserDAO
        implements InstructorDAO {

    public SQLiteInstructorDAO(DatabaseManager dbManager) {
        super(dbManager);
    }

    @Override
    public boolean insertInstructor(Instructor instructor)  {
        try {
            if (emailExists(instructor.getEmail())) {
                throw new SQLException("Email already exists in the system");
            }

            String sql = """
                INSERT INTO instructors (email, name, password_hash)
                VALUES (?, ?, ?)
            """;

            executeWrite(
                    sql,
                    instructor.getEmail(),
                    instructor.getName(),
                    instructor.getPasswordHash()
            );

            return true;
        } catch (Exception _){
            return false;
        }
    }

    @Override
    public boolean updateInstructor(Instructor instructor)  {
        try {
            String sql = """
                UPDATE instructors
                SET name = ?, email = ?, password_hash = ?
                WHERE id = ?
            """;

            executeWrite(
                    sql,
                    instructor.getName(),
                    instructor.getEmail(),
                    instructor.getPasswordHash(),
                    instructor.getId()
            );
            return true;
        } catch (Exception _){
            return false;
        }
    }

    @Override
    public Instructor findInstructorByEmail(String email)  {
        String sql = "SELECT id, email, name, password_hash FROM instructors WHERE email = ?";
        try (ResultSet rs = executeQuery(sql, email)) {
            if (rs.next()) {
                return new Instructor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password_hash")
                );
            }
        } catch (Exception _){
            return null;
        }
        return null;
    }

    @Override
    public List<Instructor> findAllInstructors()  {
        List<Instructor> instructors = new ArrayList<>();
        String sql = "SELECT id, email, name, password_hash FROM instructors";
        try (ResultSet rs = executeQuery(sql)) {
            while (rs.next()) {
                instructors.add(new Instructor(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password_hash")
                ));
            }
            return instructors;
        } catch (Exception _){
            return null;
        }
    }

    @Override
    public Instructor getInstructor(int id) {
        String sql = "SELECT id, email, name, password_hash FROM instructors WHERE id = ?";
        try (ResultSet rs = executeQuery(sql, id)) {
            if (rs.next()) {
                return new Instructor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password_hash")
                );
            }
        } catch (Exception _){
            return null;
        }
        return null;
    }
}
