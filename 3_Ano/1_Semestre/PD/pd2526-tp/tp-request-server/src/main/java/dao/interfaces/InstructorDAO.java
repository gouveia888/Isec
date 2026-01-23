package dao.interfaces;

import model.Instructor;

import java.sql.SQLException;
import java.util.List;

public interface InstructorDAO {
    boolean insertInstructor(Instructor instructor);
    boolean updateInstructor(Instructor instructor);
    Instructor getInstructor(int id);
    Instructor findInstructorByEmail(String email);
    List<Instructor> findAllInstructors();
}

