package dao.interfaces;


import java.sql.SQLException;

public interface SystemConfigDAO {

    // Database version management
    int getDatabaseVersion();
    int incrementDatabaseVersion();
    boolean setDatabaseVersion(int version);

    // Instructor code
    String getInstructorCodeHash();
    boolean setInstructorCodeHash(String hash);
}
