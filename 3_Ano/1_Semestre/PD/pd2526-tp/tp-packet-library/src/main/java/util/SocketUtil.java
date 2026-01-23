package util;

import java.io.*;
import java.net.Socket;

public class SocketUtil {
    public static boolean send(Socket socket, Serializable object) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(object);
        } catch (Exception _) {
            return false;
        }

        return true;
    }

    public static Object receive(Socket socket) {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            return in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }

    public static Object receiveWithExceptions(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        return in.readObject();
    }
}
