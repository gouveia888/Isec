package controller;

import database.DatabaseManager;
import model.Instructor;
import model.Student;
import model.User;
import network.enums.AccountType;
import network.request.*;
import network.response.EditUserResponse;
import network.response.LoginUserResponse;
import network.response.RegisterUserResponse;
import service.UserService;
import validation.UserValidator;

public class UserController {
    private final DatabaseManager databaseManager;
    private final UserService userService;

    // client
    private User activeUser;


    public UserController(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.userService = new UserService(databaseManager);
    }

    public RegisterUserResponse registerInstructor(RegisterInstructorRequest req) {
        // validation
        if (req.name() == null || req.name().isBlank()) {
            return RegisterUserResponse.failure("Name can't be empty");
        }
        if (req.email() == null || req.email().isBlank()) {
            return RegisterUserResponse.failure("Email is required");
        }
        if (!UserValidator.isEmail(req.email())) {
            return RegisterUserResponse.failure("Invalid email format");
        }
        if (req.passwordHash() == null || req.passwordHash().length() < 3) {
            return RegisterUserResponse.failure("Password must have at least 3 characters");
        }
        if (req.instructor_code() == null || req.instructor_code().isBlank()) {
            return RegisterUserResponse.failure("Missing Instructor code");
        }
        if (!userService.isInstructorCode(req.instructor_code())) {
            return RegisterUserResponse.failure("Invalid Instructor code");
        }

        Instructor instructor = new Instructor(
                -1,
                req.name(),
                req.email(),
                req.passwordHash()
        );

        boolean ok = userService.registerInstructor(instructor);

        return ok ? RegisterUserResponse.success("Instructor registered.")
                : RegisterUserResponse.failure("Email already exists");
    }

    public EditUserResponse editInstructor(EditInstructorRequest req) {
        if(activeUser == null){
            return EditUserResponse.failure("Not logged in.");
        }

        // validation
        if (req.name() == null || req.name().isBlank()) {
            return EditUserResponse.failure("Name can't be empty");
        }
        if (req.email() == null || req.email().isBlank()) {
            return EditUserResponse.failure("Email is required");
        }
        if (!UserValidator.isEmail(req.email())) {
            return EditUserResponse.failure("Invalid email format");
        }
        if (req.passwordHash() == null || req.passwordHash().length() < 3) {
            return EditUserResponse.failure("Password must have at least 3 characters");
        }

        Instructor instructor = new Instructor(
                activeUser.getId(),
                req.name(),
                req.email(),
                req.passwordHash()
        );

        boolean ok = userService.updateInstructor(instructor);

        return ok ? new EditUserResponse(true, "Instructor updated.", AccountType.INSTRUCTOR, req.name(), req.email(), "")
                : EditUserResponse.failure("Couldn't edit.");
    }

    public RegisterUserResponse registerStudent(RegisterStudentRequest req) {
        // validation
        if (req.name() == null || req.name().isBlank()) {
            return RegisterUserResponse.failure("Name can't be empty");
        }
        if (req.email() == null || req.email().isBlank()) {
            return RegisterUserResponse.failure("Email is required");
        }
        if (!UserValidator.isEmail(req.email())) {
            return RegisterUserResponse.failure("Invalid email format");
        }
        if (req.passwordHash() == null || req.passwordHash().length() < 3) {
            return RegisterUserResponse.failure("Password must have at least 3 characters");
        }
        if (req.studentCode() == null || req.studentCode().isBlank()) {
            return RegisterUserResponse.failure("Missing student number");
        }

        Student student = new Student(
                -1,
                req.name(),
                req.email(),
                req.passwordHash(),
                req.studentCode()
        );

        boolean ok = userService.registerStudent(student);

        return ok ? RegisterUserResponse.success("Student registered.")
                : RegisterUserResponse.failure("Email already exists");
    }

    public EditUserResponse editStudent(EditStudentRequest req) {
        // validation
        if (req.name() == null || req.name().isBlank()) {
            return EditUserResponse.failure("Name can't be empty");
        }
        if (req.email() == null || req.email().isBlank()) {
            return EditUserResponse.failure("Email is required");
        }
        if (!UserValidator.isEmail(req.email())) {
            return EditUserResponse.failure("Invalid email format");
        }
        if (req.passwordHash() == null || req.passwordHash().length() < 3) {
            return EditUserResponse.failure("Password must have at least 3 characters");
        }
        if (req.studentCode() == null || req.studentCode().isBlank()) {
            return EditUserResponse.failure("Missing student number");
        }

        Student student = new Student(
                activeUser.getId(),
                req.name(),
                req.email(),
                req.passwordHash(),
                req.studentCode()
        );

        boolean ok = userService.updateStudent(student);

        return ok ? new EditUserResponse(true, "Student updated.", AccountType.STUDENT, req.name(), req.email(), req.studentCode())
                : EditUserResponse.failure("Email or Student number already exists");
    }

    public LoginUserResponse loginUser(LoginUserRequest req) {

        // validation
        if (req.email() == null || req.email().isBlank()) {
            return LoginUserResponse.failure("Email is required");
        }

        User user = userService.login(req.email(), req.password());
        if (user == null) {
            return LoginUserResponse.failure("Invalid email or password");
        }
        AccountType accountType = user.getClass().equals(Student.class) ? AccountType.STUDENT : AccountType.INSTRUCTOR;
        this.activeUser = user;

        String studentNumber = "";
        if(user instanceof Student student) {
            studentNumber = student.getStudentNumber();
        }
        return LoginUserResponse.success(accountType, user.getName(), user.getEmail(), studentNumber);
    }

    public User getActiveUser() {
        return activeUser;
    }

    public Instructor getInstructor(int id){
        return userService.getInstructor(id);
    }

    public Student getStudent(int id){
        return userService.getStudent(id);
    }
}