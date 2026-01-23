import javafx.application.Application;
import ui.QuizApp;


public class Main {
    public static void main(String[] args) {
        // Clients are launched with the address and port of the directory server as command line arguments.
        // The client should request the server they will connect to from the directory server.
        // After they request the email and password from the user for authentication or registration.
        // The client should then send the authentication or registration request to the server
        //   If it fails, or the client takes too long to respond, the server should shut down the connection
        // When the connection is lost, the client should request a new server from the directory server and reconnect.
        //   This should happen transparently to the user.
        // The views should be implemented using JavaFX and updated asynchronously.
        // The code should be structured into 2 distinct packages:
        //   Communication: all code related to networking and communication with the servers
        //   View: user interface and interaction

        Application.launch(QuizApp.class, args);
    }
}
