package config;

import util.FancyLog;

import java.io.File;
import java.net.InetAddress;

public class ServerConfig {
    private final String directoryServerAddress;
    private final int directoryServerPort;
    private final String databaseDirectory;
    private final String multicastAddress;

    private ServerConfig(String directoryServerAddress, int directoryServerPort,
                        String databaseDirectory, String multicastAddress) {
        this.directoryServerAddress = directoryServerAddress;
        this.directoryServerPort = directoryServerPort;
        this.databaseDirectory = databaseDirectory;
        this.multicastAddress = multicastAddress;
    }

    public String getDirectoryServerAddress() {
        return directoryServerAddress;
    }

    public int getDirectoryServerPort() {
        return directoryServerPort;
    }

    public String getDatabaseDirectory() {
        return databaseDirectory;
    }

    public String getMulticastAddress() {
        return multicastAddress;
    }

    // Validation logic
    public static ServerConfig fromArgs(String[] args) throws IllegalArgumentException {
        if (args.length != 4) {
            printUsage();
        }

        String dirServerAddress = args[0];
        int dirServerPort;
        String dbDir = args[2];
        String multicastAddr = args[3];

        // Parse the port
        try {
            dirServerPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            FancyLog.println("Error: directoryServerPort must be a number.", FancyLog.Status.FAILED);
            printUsage();
            throw new IllegalArgumentException();
        }

        // Check if it's a directory
        File dbFolder = new File(dbDir);
        if (!dbFolder.exists() || !dbFolder.isDirectory()) {
            FancyLog.println("Error: database directory does not exist: " + dbDir, FancyLog.Status.FAILED);
            throw new IllegalArgumentException();
        }

        // Check it the address is really multicast
        try {
            InetAddress addr = InetAddress.getByName(multicastAddr);
            if (!addr.isMulticastAddress()) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            FancyLog.println("Error: invalid multicast address: " + multicastAddr, FancyLog.Status.FAILED);
            throw new IllegalArgumentException();
        }

        FancyLog.println("Server arguments validated successfully.", FancyLog.Status.OK);
        return new ServerConfig(dirServerAddress, dirServerPort, dbDir, multicastAddr);
    }

    private static void printUsage() {
        FancyLog.println("Usage: java Main <directoryServerAddress> <directoryServerPort> <databaseDirectory> <multicastAddress>", FancyLog.Status.INFO);
    }
}