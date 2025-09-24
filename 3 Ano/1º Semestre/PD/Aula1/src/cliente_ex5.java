import java.io.IOException;
import java.net.*;

public class cliente_ex5 {
    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";
    public  static final int TIMEOUT = 10;
    public static void main(String[] args) {
        InetAddress serverAddress = null; // a class InetAdres encapsula um IP
        DatagramPacket packet = null;
        int serverPort;
        String response;

        if(args.length != 2){
            System.out.println("Usage : java ex5.ex5 <server_address> <server_port>");
            System.exit(1);
        }

        try(DatagramSocket socket = new DatagramSocket()){
            serverPort = Integer.parseInt(args[1]);
            serverAddress = InetAddress.getByName(args[0]);
            socket.setSoTimeout(TIMEOUT*10000);
            packet = new DatagramPacket(TIME_REQUEST.getBytes(), TIME_REQUEST.length(), serverAddress, serverPort);

            socket.send(packet);


            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            socket.receive(packet);


            response = new String(packet.getData(), 0, packet.getLength());
            System.out.println(response);

        }

        catch (UnknownHostException e) {
            throw new RuntimeException(e);

        }
        catch (NumberFormatException e) {
            System.out.println("Invalid server port");
        }
        catch (SocketTimeoutException e) {
            System.out.println("Server timed out");
        }
        catch (SocketException e) {
            throw new RuntimeException(e);

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}