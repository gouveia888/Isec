package model.data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Base class for all users in the system (Instructor and Student).
 */
public abstract class User {
    protected String name;
    protected String email;
    protected String password;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}



