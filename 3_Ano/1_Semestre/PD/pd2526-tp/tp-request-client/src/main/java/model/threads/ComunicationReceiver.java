package model.threads;

import model.AuthenticatorService;
import model.QuizAppManager;
import network.response.LoginUserResponse;
import util.FancyLog;
import util.SocketUtil;

import java.io.IOException;
import java.net.Socket;

public class ComunicationReceiver implements Runnable {
    private final QuizAppManager appManager;
    private final AuthenticatorService authenticatorService;
    private Socket serverSocket;

    public ComunicationReceiver(QuizAppManager appManager) {
        this.appManager = appManager;
        this.authenticatorService = appManager.getAuthenticatorService();
        this.serverSocket = appManager.getActiveServerSocket();
    }

    @Override
    public void run() {
        FancyLog.println("Started ClientConnectionReceiver thread.", FancyLog.Status.OK);
        while(appManager.getIsRunning()) {
            try {

                Object object;
                synchronized (appManager.getActiveServerSocket()) {
                    object = SocketUtil.receiveWithExceptions(appManager.getActiveServerSocket());
                }
                appManager.handleResponse(object);


            } catch (IOException e) {
                if(!authenticatorService.isLogged()){
                    FancyLog.println("Failed to login in 30 seconds. Terminating.");
                    appManager.fireShutdown("Failed to login in 30 seconds. Terminating.");
                    break;
                }
                else {
                    appManager.externalReconnect();
                    Object o = SocketUtil.receive(appManager.getActiveServerSocket());
                    if(o instanceof LoginUserResponse){
                        appManager.handleResponse(o);
                        if (!appManager.isUserLogged()){
                            FancyLog.println("Couldn't reconnect to the server. Shutting down");
                            appManager.fireShutdown("Couldn't reconnect to the server. Shutting down");
                            break;
                        }
                    }
                    else{
                        FancyLog.println("Lost connection to the server.");
                        appManager.fireShutdown("Lost connection to the server.");
                        break;
                    }

                }

            } catch (ClassNotFoundException e) {
                FancyLog.println("Received an invalid data from the server. Terminating.");
                appManager.fireShutdown("Received an invalid data from the server. Terminating.");
                break;
            }
        }
        FancyLog.println("Closed ClientConnectionReceiver thread.");
    }

}
