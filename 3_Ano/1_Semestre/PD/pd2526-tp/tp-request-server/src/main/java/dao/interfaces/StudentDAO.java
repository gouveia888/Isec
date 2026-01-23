package dao.interfaces;

import model.Student;

import java.sql.SQLException;
import java.util.List;

public interface StudentDAO {
    boolean insertStudent(Student student);
    boolean updateStudent(Student student);
    Student findStudentByEmail(String email);
    Student getStudent(int id);
    List<Student> findAllStudents();
}