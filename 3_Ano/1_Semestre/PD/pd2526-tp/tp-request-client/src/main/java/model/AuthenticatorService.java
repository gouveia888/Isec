package model;

import network.enums.AccountType;
import network.request.*;
import network.response.EditUserResponse;
import network.response.LoginUserResponse;
import network.response.RegisterUserResponse;
import util.SecurityUtils;

public class AuthenticatorService {
    // manage
    QuizAppManager appManager;

    // data
    private boolean isLogged;
    private AccountType accountType;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String studentNumber;

    private String tempPassword;

    public AuthenticatorService(QuizAppManager appManager) {
        this.appManager = appManager;
        isLogged = false;
    }

    public void registerInstructor(String email, String password, String name, String instructorCode) {
        RegisterInstructorRequest register = new RegisterInstructorRequest(
                name,
                email,
                SecurityUtils.sha256(password),
                SecurityUtils.sha256(instructorCode)
        );
        appManager.sendToServer(register);
    }

    public void editInstructor(String email, String password, String name) {
        EditInstructorRequest register = new EditInstructorRequest(
                name,
                email,
                SecurityUtils.sha256(password)
        );
        tempPassword = password;
        appManager.sendToServer(register);
    }

    public void registerStudent(String email, String password, String name, String studentNumber) {
        RegisterStudentRequest register = new RegisterStudentRequest(
                name,
                email,
                SecurityUtils.sha256(password),
                studentNumber
        );
        appManager.sendToServer(register);
    }


    public void editStudent(String email, String password, String name, String studentNumber) {
        EditStudentRequest register = new EditStudentRequest(
                name,
                email,
                SecurityUtils.sha256(password),
                studentNumber
        );
        appManager.sendToServer(register);
    }

    public void authenticate(String email, String password) {
        LoginUserRequest request = new LoginUserRequest(email, SecurityUtils.sha256(password));
        userEmail = email;
        userPassword = password;
        appManager.sendToServerWithoutRetry(request);
    }

    // responses

    public void registerResponse(RegisterUserResponse response) {

    }

    public void authenticateResponse(LoginUserResponse response) {
        if(response.success()) {
            isLogged = true;
            userName = response.username();
            studentNumber = response.studentNumber();
            accountType = response.accountType();
        }
        else {
            isLogged = false;
            userName = "";
            studentNumber = "";
        }
    }


    public void editUserResponse(EditUserResponse response) {
        if(response.success()){
            userName = response.username();
            userEmail = response.email();
            userPassword = tempPassword;
            studentNumber = response.studentNumber();
            accountType = response.accountType();
        }
    }


    public boolean isLogged() {
        return isLogged;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void reconnectUser() {
        authenticate(userEmail, userPassword);
    }
}