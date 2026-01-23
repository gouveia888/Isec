package network.registry;

import network.request.RegisterServerRequest;
import network.request.UnregisterServerNotice;
import network.response.RegisterServerResponse;
import util.DatagramUtil;
import util.FancyLog;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class DirectoryRegistrar {

    //private final ServerConfig config;
    private final InetSocketAddress directorySocketAddress;
    private final int serverPort;
    private final int clientPort;

    private String myAddress;

    public DirectoryRegistrar(InetSocketAddress directorySocketAddress, int serverPort, int clientPort) {
        this.directorySocketAddress = directorySocketAddress;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
    }

    public RegisterServerResponse register() {
        FancyLog.println("Started registration with the DirectoryServer.");
        RegisterServerRequest registerRequest = new RegisterServerRequest(serverPort, clientPort);

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000);
//            SocketAddress directoryAddress = new InetSocketAddress(
//                    config.getDirectoryServerAddress(),
//                    config.getDirectoryServerPort()
//            );

            FancyLog.println("Sending registration request...");
            if (!DatagramUtil.send(socket, registerRequest, directorySocketAddress)) {
                FancyLog.println("Couldn't connect to the DirectoryServer.", FancyLog.Status.FAILED);
                return null;
            }

            FancyLog.println("Awaiting registration response...");
            RegisterServerResponse response = (RegisterServerResponse) DatagramUtil.receive(socket, directorySocketAddress);
            myAddress = response.requesterAddress();
            return response;

        } catch (Exception e) {
            FancyLog.println("Couldn't receive a response from the DirectoryServer.", FancyLog.Status.FAILED);
            return null;
        }
    }

    public void unregister(){
        try{
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(5000);

            UnregisterServerNotice notice = new UnregisterServerNotice(myAddress, serverPort, clientPort);
            DatagramUtil.send(socket, notice , directorySocketAddress);
        } catch (Exception _){

        }
    }
}