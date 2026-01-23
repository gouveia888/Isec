package service;

import dao.interfaces.InstructorDAO;
import dao.interfaces.StudentDAO;
import dao.interfaces.SystemConfigDAO;
import dao.sqlite.SQLiteInstructorDAO;
import dao.sqlite.SQLiteStudentDAO;
import dao.sqlite.SQLiteSystemConfigDAO;
import database.DatabaseManager;
import model.Instructor;
import model.Student;
import model.User;

public class UserService {
    private final DatabaseManager databaseManager;
    private final InstructorDAO instructorDAO;
    private final StudentDAO studentDAO;
    private final SystemConfigDAO systemConfigDAO;

    public UserService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

        this.instructorDAO = new SQLiteInstructorDAO(databaseManager);
        this.studentDAO = new SQLiteStudentDAO(databaseManager);
        this.systemConfigDAO = new SQLiteSystemConfigDAO(databaseManager);
    }

    public boolean registerInstructor(Instructor instructor) {
        // instructor and student cannot share emails
        if (studentDAO.findStudentByEmail(instructor.getEmail()) != null ||
            instructorDAO.findInstructorByEmail(instructor.getEmail()) != null)
            return false;


        return instructorDAO.insertInstructor(instructor);
    }

    public boolean updateInstructor(Instructor instructor) {
        // not student should have that email
        if(studentDAO.findStudentByEmail(instructor.getEmail()) != null)
            return false;

        // check if an instructor has that email, other than itself
        Instructor searchInstructor = instructorDAO.findInstructorByEmail(instructor.getEmail());
        if(searchInstructor != null && searchInstructor.getId() != instructor.getId()){
            return false;
        }

        return instructorDAO.updateInstructor(instructor);
    }

    public User login(String email, String passwordHash) {
        Student student = studentDAO.findStudentByEmail(email);
        if (student != null && passwordHash.equals(student.getPasswordHash())) {
            return student;
        }

        Instructor instructor = instructorDAO.findInstructorByEmail(email);
        if (instructor != null && passwordHash.equals(instructor.getPasswordHash())) {
            return instructor;
        }

        return null;
    }
    // helper

    public boolean isInstructorCode(String code) {
        return code.equals(systemConfigDAO.getInstructorCodeHash());
    }

    public boolean registerStudent(Student student) {
        // instructor and student cannot share emails
        if (studentDAO.findStudentByEmail(student.getEmail()) != null ||
                instructorDAO.findInstructorByEmail(student.getEmail()) != null)
            return false;

        return studentDAO.insertStudent(student);
    }

    public boolean updateStudent(Student student) {
        // no instructor can have the same email
        if(instructorDAO.findInstructorByEmail(student.getEmail()) != null)
            return false;

        // check if a student has that email, other than itself
        Student searchStudent = studentDAO.findStudentByEmail(student.getEmail());
        if(searchStudent != null && searchStudent.getId() != student.getId()){
            return false;
        }

        return studentDAO.updateStudent(student);
    }

    public Instructor getInstructor(int id) {
        return instructorDAO.getInstructor(id);
    }

    public Student getStudent(int id) {
        return studentDAO.getStudent(id);
    }
}


