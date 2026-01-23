package model;

import model.threads.ComunicationReceiver;
import model.util.ValidationUtil;
import network.request.LoginUserRequest;
import network.response.*;
import util.FancyLog;
import util.SocketUtil;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class QuizAppManager {

    // state
    private AtomicBoolean isRunning;

    // services
    private final QuizService quizService;
    private final DirectoryService directoryService;
    private final AuthenticatorService authenticatorService;

    // data
    private final InetSocketAddress directoryServerSocketAddress;
    private InetSocketAddress activeServerSocketAddress;
    private Socket activeServerSocket;

    // event emitter
    private final PropertyChangeSupport pcs;

    // shutdown
    public static String CLIENT_SHUTDOWN = "CLIENT_SHUTDOWN";
    // auth
    public static String REGISTER_USER_RESPONSE = "REGISTER_USER_RESPONSE";
    public static String EDIT_USER_RESPONSE = "EDIT_USER_RESPONSE";
    public static String LOGIN_USER_RESPONSE = "LOGIN_USER_RESPONSE";
    // instructor responses
    public static String CREATE_QUESTION_RESPONSE = "CREATE_QUESTION_RESPONSE";
    public static String EDIT_QUESTION_RESPONSE = "EDIT_QUESTION_RESPONSE";
    public static String DELETE_QUESTION_RESPONSE = "DELETE_QUESTION_RESPONSE";
    public static String LIST_INSTRUCTOR_QUESTIONS_RESPONSE = "LIST_INSTRUCTOR_QUESTIONS_RESPONSE";
    public static String VIEW_QUESTION_STATISTICS_RESPONSE = "VIEW_QUESTION_STATISTICS_RESPONSE";
    // student responses
    public static String SHOW_QUESTION_OPTIONS_RESPONSE = "SHOW_QUESTION_OPTIONS_RESPONSE";
    public static String SUBMIT_QUESTION_ANSWER_RESPONSE = "SUBMIT_QUESTION_ANSWER_RESPONSE";
    public static String SHOW_QUESTIONS_ANSWER_RESPONSE = "SHOW_QUESTIONS_ANSWER_RESPONSE";

    private final Map<Class<?>, String> notificationMap = Map.ofEntries(
            Map.entry(RegisterUserResponse.class, REGISTER_USER_RESPONSE),
            Map.entry(EditUserResponse.class, EDIT_USER_RESPONSE),
            Map.entry(LoginUserResponse.class, LOGIN_USER_RESPONSE),

            Map.entry(CreateQuestionResponse.class, CREATE_QUESTION_RESPONSE),
            Map.entry(EditQuestionResponse.class, EDIT_QUESTION_RESPONSE),
            Map.entry(DeleteQuestionResponse.class, DELETE_QUESTION_RESPONSE),
            Map.entry(ListInstructorQuestionsResponse.class, LIST_INSTRUCTOR_QUESTIONS_RESPONSE),
            Map.entry(ViewQuestionStatisticsResponse.class, VIEW_QUESTION_STATISTICS_RESPONSE),

            Map.entry(ShowQuestionOptionsResponse.class, SHOW_QUESTION_OPTIONS_RESPONSE),
            Map.entry(SubmitQuestionAnswerResponse.class, SUBMIT_QUESTION_ANSWER_RESPONSE),
            Map.entry(ShowQuestionsAnswerResponse.class, SHOW_QUESTIONS_ANSWER_RESPONSE)
    );


    public QuizAppManager(List<String> args) throws Exception {
        directoryServerSocketAddress = ValidationUtil.parseDirectoryServerSocketAddress(args);

        isRunning = new AtomicBoolean(true);
        quizService = new QuizService(this);
        directoryService = new DirectoryService(this);
        authenticatorService = new AuthenticatorService(this);
        pcs = new PropertyChangeSupport(this);

        init();
    }


    public void init() throws Exception {
        // Query the directory server for the active server
        ActiveServerResponse response = directoryService.getActiveServer();
        if (response == null) {
            throw new Exception("Could not retrieve active server from directory service.");
        }
        if (response.port() == -1) {
            throw new Exception("There is no server running.");
        }
        activeServerSocketAddress = new InetSocketAddress(response.address(), response.port());

        // Create a socket to connect to the active server
        activeServerSocket = new Socket(activeServerSocketAddress.getAddress().getHostAddress(), activeServerSocketAddress.getPort());
        //activeServerSocket.setSoTimeout(5000);

        // Create threads
        ComunicationReceiver comunicationReceiver = new ComunicationReceiver(this);
        Thread comunicationReceiverThread = new Thread(comunicationReceiver);
        comunicationReceiverThread.start();
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }


    public QuizService getQuizService() {
        return quizService;
    }

    public DirectoryService getDirectoryService() {
        return directoryService;
    }

    public AuthenticatorService getAuthenticatorService() {
        return authenticatorService;
    }

    public synchronized Socket getActiveServerSocket() {
        return activeServerSocket;
    }

    public synchronized void setActiveServerSocket(Socket activeServerSocket) {
        this.activeServerSocket = activeServerSocket;
    }

    public InetSocketAddress getDirectoryServerSocketAddress() {
        return directoryServerSocketAddress;
    }

    public boolean getIsRunning() {
        return isRunning.get();
    }

    public void fire(Object data) {
        if (data == null) return; // or fire some invalid?
        String notification_type = notificationMap.get(data.getClass());
        if (notification_type == null) return;
        pcs.firePropertyChange(notification_type, "", data);
    }

    public void fireShutdown(String message){
        pcs.firePropertyChange(CLIENT_SHUTDOWN, "", message);
    }

    public void handleResponse(Object object) {
        if (object == null) {
            return;
        }
        System.out.println("Received " + object.getClass().getSimpleName() + " from server.");
        // First, we handle the request
        switch (object) {
            // auth
            case RegisterUserResponse response -> authenticatorService.registerResponse(response);
            case EditUserResponse response -> authenticatorService.editUserResponse(response);
            case LoginUserResponse response -> authenticatorService.authenticateResponse(response);

            // Instructor questions
            case CreateQuestionResponse response -> quizService.createQuestionResponse(response);
            case EditQuestionResponse response -> quizService.editQuestionResponse(response);
            case DeleteQuestionResponse response -> quizService.deleteQuestionResponse(response);
            case ListInstructorQuestionsResponse response -> quizService.listInstructorQuestionsResponse(response);
            case ViewQuestionStatisticsResponse response -> quizService.viewQuestionStatisticsResponse(response);

            //Student questions
            case ShowQuestionOptionsResponse response -> quizService.showQuestionResponse(response);
            case SubmitQuestionAnswerResponse response -> quizService.submitAnswerResponse(response);
            case ShowQuestionsAnswerResponse response -> quizService.showQuestionsAnswersResponse(response);


            default -> FancyLog.println("Received invalid packet. Ignoring", FancyLog.Status.FAILED);
        }

        // Then we fire the property
        fire(object);
    }

    public boolean isUserLogged(){
        return authenticatorService.isLogged();
    }

    private synchronized boolean reconnect() {
        try {
            ActiveServerResponse response = directoryService.getActiveServer();
            if (response == null) return false;

            activeServerSocketAddress = new InetSocketAddress(response.address(), response.port());
            setActiveServerSocket(new Socket(response.address(), response.port()));
            return true;
        } catch (Exception _) {
            return false;
        }
    }

    public void shutdown() {
        try {
            isRunning.set(false);
            activeServerSocket.close();
        } catch (Exception _) {
        }
    }

    public void externalReconnect() {
        reconnect();
        authenticatorService.reconnectUser();
    }

    public void sendToServer(Serializable packet) {
        boolean messageSent = false;
        int retryCount = 0;

        try {
            while (retryCount < 10) {
                // If we send the message right away, there's nothing else to do
                messageSent = SocketUtil.send(getActiveServerSocket(), packet);
                if (messageSent) {
                    System.out.println("Send" + packet.getClass().getSimpleName() + " to server.");
                    return;
                }

                // The first time it fails we just leave a debug message
                if (retryCount == 1) {
                    FancyLog.println("Lost connection to the server. Trying to reconnect");
                }

                // We retry a couple of times
                reconnect();
                authenticatorService.reconnectUser();
                retryCount += 1;
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {

        }

        // If we reached the end of the retries, then we failed and need to shutdown
        FancyLog.println("Couldn't reconnect. Shutting down.");
        shutdown();
    }

    public void sendToServerWithoutRetry(LoginUserRequest request) {
        SocketUtil.send(getActiveServerSocket(), request);
    }
}
