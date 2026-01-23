package dao.sqlite;

import dao.BaseDAO;
import dao.interfaces.StudentDAO;
import database.DatabaseManager;
import model.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteStudentDAO
    extends SQLiteUserDAO
    implements StudentDAO
{

    public SQLiteStudentDAO(DatabaseManager dbManager) {
        super(dbManager);
    }

    @Override
    public boolean insertStudent(Student student)  {
        try {
            if (emailExists(student.getEmail())) {
                throw new SQLException("Email already exists in the system");
            }
            String sql = """
                INSERT INTO students (email, student_number, name, password_hash)
                VALUES (?, ?, ?, ?)
            """;
            executeWrite(
                    sql,
                    student.getEmail(),
                    student.getStudentNumber(),
                    student.getName(),
                    student.getPasswordHash()
            );
            return true;
        } catch (Exception _){
            return false;
        }
    }

    @Override
    public boolean updateStudent(Student student)  {
        try {
            String sql = """
                UPDATE students
                SET name = ?, email = ?, student_number = ?, password_hash = ?
                WHERE id = ?
            """;

            executeWrite(
                    sql,
                    student.getName(),
                    student.getEmail(),
                    student.getStudentNumber(),
                    student.getPasswordHash(),
                    student.getId()
            );

            return true;
        }catch (Exception _){
            return false;
        }
    }

    @Override
    public Student findStudentByEmail(String email)  {
        String sql = "SELECT id, email, student_number, name, password_hash FROM students WHERE email = ?";
        try (ResultSet rs = executeQuery(sql, email)) {
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("student_number")
                );
            }
        } catch (Exception _){
            return null;
        }
        return null;
    }

    @Override
    public List<Student> findAllStudents()  {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, email, student_number, name, password_hash FROM students";
        try (ResultSet rs = executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("student_number"),
                        rs.getString("name"),
                        rs.getString("password_hash")
                ));
            }
            return students;
        } catch (Exception _){
            return null;
        }
    }

    @Override
    public Student getStudent(int id) {
        String sql = "SELECT id, email, student_number, name, password_hash FROM students WHERE id = ?";
        try (ResultSet rs = executeQuery(sql, id)) {
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("student_number")
                );
            }
        } catch (Exception _){
            return null;
        }
        return null;
    }
}
