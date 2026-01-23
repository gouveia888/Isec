package model.data;

/**
 * Represents a student profile.
 */
public class Student extends User {
    private int studentNumber;

    public Student(int studentNumber, String name, String email) {
        super(name, email);
        this.studentNumber = studentNumber;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }
}