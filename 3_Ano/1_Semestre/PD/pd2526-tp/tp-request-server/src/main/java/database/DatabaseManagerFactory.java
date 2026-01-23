package database;

import dao.interfaces.SystemConfigDAO;
import dao.sqlite.SQLiteSystemConfigDAO;
import util.SecurityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

public class DatabaseManagerFactory {
    private static final String DB_PREFIX = "app_db_";

    /**
     * Load the most recent DB in the directory or create a new one if none exists.
     */
    public static DatabaseManager loadLatestOrCreateNew(String directory, String defaultInstructorCode) throws SQLException {
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".db"));
        if (files != null && files.length > 0) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
            return new DatabaseManager(files[0].getAbsolutePath());
        }
        // No DB found, create new
        return createNewDatabase(directory, defaultInstructorCode);
    }

    /**
     * Create a new database and initialize tables and default config.
     */
    public static DatabaseManager createNewDatabase(String directory, String defaultInstructorCode) throws SQLException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String dbPath = directory + "/" + DB_PREFIX + timestamp + ".db";
        DatabaseManager dbManager = new DatabaseManager(dbPath);

        // Create tables and put the initial values
        dbManager.createTables();
        SystemConfigDAO systemConfigDAO = new SQLiteSystemConfigDAO(dbManager);
        systemConfigDAO.setDatabaseVersion(1);
        systemConfigDAO.setInstructorCodeHash(SecurityUtils.sha256(defaultInstructorCode));

        return dbManager;
    }

    public static void clearDatabaseFiles(String databaseDirectory) {
        // Check if it's actually a directory
        Path databasePath = Path.of(databaseDirectory);
        if (!Files.isDirectory(databasePath)) return;


        try (Stream<Path> files = Files.list(databasePath)) {

            // Find the most recent file
            Optional<Path> mostRecentFileOptional = files.max((o1, o2) -> {
                try {
                    return Files.getLastModifiedTime(o1).compareTo(Files.getLastModifiedTime(o2));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            if (mostRecentFileOptional.isEmpty())
                return;
            Path mostRecentFile = mostRecentFileOptional.get();

            // Remove all others
            Stream<Path> filesAgain = Files.list(databasePath);
            filesAgain.filter(path -> !path.equals(mostRecentFile)).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });


        } catch (IOException _) {
        }
    }
}
