package database;

import dao.interfaces.SystemConfigDAO;
import dao.sqlite.SQLiteSystemConfigDAO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;


public class DatabaseManager {
    private final String dbFilePath;
    private Connection connection;
    private final Object dbLock = new Object(); // lock for sync

    public DatabaseManager(String dbFilePath) throws SQLException {
        this.dbFilePath = dbFilePath;
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
    }

    public Connection getConnection() {
        return connection;
    }

    public String getDbFilePath() {
        return dbFilePath;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Thread-safe method to execute an SQL update (INSERT, UPDATE, DELETE, CREATE, etc.)
     * and optionally log it for replication.
     */
    // this function is only use for received queries
    public boolean executeNetworkQuery(String sql) {
        synchronized (dbLock) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(sql);
                SystemConfigDAO dao = new SQLiteSystemConfigDAO(this);
                if (dao.incrementDatabaseVersion() == -1) return false;
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    /**
     * Get the database file as bytes.
     */
    public byte[] getDatabaseBytes() throws IOException, SQLException {
        synchronized (dbLock) {
            try (Statement stmt = connection.createStatement()) {
                // acquire write lock
                // we don't need this because we have the sync,
                // but it seems to be good practice
                stmt.execute("BEGIN IMMEDIATE;");
                File file = new File(dbFilePath);
                byte[] bytes = Files.readAllBytes(file.toPath());
                stmt.execute("COMMIT;");
                return bytes;
            }
        }
    }

    /**
     * Set the database bytes
     */
    public void setDatabaseBytes(byte[] dbBytes) throws IOException, SQLException {
        synchronized (dbLock) {
            // 1️⃣ Close the current connection if open
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }

            // 2️⃣ Overwrite the database file
            try (FileOutputStream fos = new FileOutputStream(dbFilePath)) {
                fos.write(dbBytes);
                fos.flush();
            }

            // 3️⃣ Reopen the connection to the new database file
            openConnection(); // Your method to initialize `connection = DriverManager.getConnection(...)`
        }
    }


    /**
     * Returns the lock object for external use
     */
    public Object getDbLock() {
        return dbLock;
    }

    public void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {

            // Student table
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS students (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        email TEXT UNIQUE NOT NULL,
                        student_number TEXT UNIQUE NOT NULL,
                        name TEXT NOT NULL,
                        password_hash TEXT NOT NULL
                    )
                    """);

            // Instructor table
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS instructors (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                            email TEXT UNIQUE NOT NULL,
                        name TEXT NOT NULL,
                        password_hash TEXT NOT NULL
                    )
                    """);

            // System config table
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS system_config (
                        key TEXT PRIMARY KEY,
                        value TEXT NOT NULL
                    )
                    """);

            // Question table
            //access_code TEXT UNIQUE NOT NULL,
            // ^ the question id can be de access_code
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS questions (
                        question_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        statement TEXT NOT NULL,
                        start_time TEXT NOT NULL,
                        end_time TEXT NOT NULL,
                        instructor_id INTEGER NOT NULL,
                        correct_option_index INTEGER NOT NULL,
                        FOREIGN KEY (instructor_id) REFERENCES instructors(id)
                    );
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS question_options (
                        question_id INTEGER NOT NULL,
                        option_index INTEGER NOT NULL,
                        option_text TEXT NOT NULL,
                        PRIMARY KEY (question_id, option_index),
                        FOREIGN KEY (question_id) REFERENCES questions(question_id)
                    );
                    """);


            // the DELETE CASCADE makes sure the responses are deleted when
            // Student answers table
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS student_answers (
                        answer_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        question_id INTEGER NOT NULL,
                        student_id INTEGER NOT NULL,
                        selected_option_index INTEGER NOT NULL,
                        UNIQUE(question_id, student_id),
                        FOREIGN KEY(question_id) REFERENCES questions(question_id) ON DELETE CASCADE,
                        FOREIGN KEY(student_id) REFERENCES students(id)
                    );
                    """);
        }
    }


    public synchronized long incrementVersion() throws SQLException {
        SystemConfigDAO configDAO = new SQLiteSystemConfigDAO(this);
        return configDAO.incrementDatabaseVersion();
    }


    public void openConnection() throws SQLException {
        synchronized (dbLock) {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);

            // enable foreign keys for consistency
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("PRAGMA foreign_keys = ON;");
//            }
        }
    }


}

