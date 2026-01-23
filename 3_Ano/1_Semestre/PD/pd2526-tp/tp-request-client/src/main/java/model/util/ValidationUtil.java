package model.util;

import util.FancyLog;

import java.net.InetSocketAddress;
import java.util.List;

public class ValidationUtil {

    public static InetSocketAddress parseDirectoryServerSocketAddress(List<String> args){
        if (args.size() != 2) {
            printUsage();
        }
        String address;
        int port;

        try{
            address = args.get(0);
            port = Integer.parseInt(args.get(1));
        } catch (NumberFormatException _){
            FancyLog.println("Error: directoryServerPort must be a number.", FancyLog.Status.FAILED);
            printUsage();
            throw new IllegalArgumentException();
        }

        InetSocketAddress directorySocketAddress;
        try{
            directorySocketAddress = new InetSocketAddress(address, port);
        } catch (Exception _){
            FancyLog.println("Invalid socket address." , FancyLog.Status.FAILED);
            printUsage();
            throw new IllegalArgumentException();
        }
        return directorySocketAddress;
    }

    private static void printUsage() {
        FancyLog.println("Usage: java Main <directoryServerAddress> <directoryServerPort>");
    }
}
