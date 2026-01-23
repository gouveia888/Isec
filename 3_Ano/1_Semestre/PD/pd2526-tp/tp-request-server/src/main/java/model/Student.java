package model;


public class Student extends User {

    private String studentNumber;

    public Student(int id, String name, String email, String passwordHash, String studentNumber) {
        super(id, name, email, passwordHash);
        this.studentNumber = studentNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    @Override
    public String toString() {
        return "Student{" +
               "id='" + getId() + '\'' +
               "name='" + getName() + '\'' +
               ", email='" + getEmail() + '\'' +
               ", studentNumber='" + studentNumber + '\'' +
               '}';
    }
}