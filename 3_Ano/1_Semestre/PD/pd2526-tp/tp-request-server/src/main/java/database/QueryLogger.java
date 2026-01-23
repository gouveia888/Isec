package database;

import network.request.HeartbeatRequest;
import util.DatagramUtil;

import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Thread-safe in-memory log of SQL queries using synchronized methods.
 */
public class QueryLogger {

    private static final List<LogEntry> log = new ArrayList<>();

    public static final Object logLock = new Object();

    public static synchronized void log(long version, String finalSql) {
        log.add(new LogEntry(version, finalSql));
    }


    public static synchronized void sendLogs(
            DatagramSocket socket, SocketAddress multicastAddress,
            String serverAddress, int serverReceiverPort, int clientReceiverPort) {
        for (LogEntry logEntry : log) {
            HeartbeatRequest heartbeat = new HeartbeatRequest(
                    (int) logEntry.version,
                    logEntry.sql(),
                    serverReceiverPort,
                    clientReceiverPort,
                    serverAddress
            );
            DatagramUtil.send(socket, heartbeat, multicastAddress);
        }
        log.clear();
    }

    public record LogEntry(long version, String sql) {
    }
}