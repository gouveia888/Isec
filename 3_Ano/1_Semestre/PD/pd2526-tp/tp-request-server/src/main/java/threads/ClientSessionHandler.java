package threads;

import controller.QuestionController;
import controller.UserController;
import database.DatabaseManager;
import database.QueryLogger;
import network.ServerManager;
import network.request.*;
import network.response.*;
import util.FancyLog;
import util.SocketUtil;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

public class ClientSessionHandler implements Runnable {

    // state
    private Phase phase = Phase.PRE_LOGIN;

    // server management
    private final ServerManager serverManager;
    private final DatabaseManager databaseManager;
    private final UserController userController;
    private final QuestionController questionController;

    private final SocketAddress multicastSocketAddress;
    private final int serverReceiverPort;
    private final int clientReceiverPort;

    private final DatagramSocket datagramSocket;

    // client communication
    private final Socket clientSocket;

    public ClientSessionHandler(
            ServerManager serverManager,
            DatabaseManager databaseManager,
            QuestionController questionController,
            Socket clientSocket,
            SocketAddress multicastSocketAddress,
            int serverReceiverPort,
            int clientReceiverPort
    ) throws SocketException {
        this.serverManager = serverManager;
        this.databaseManager = databaseManager;
        this.questionController = questionController;
        this.clientSocket = clientSocket;
        this.multicastSocketAddress = multicastSocketAddress;
        this.serverReceiverPort = serverReceiverPort;
        this.clientReceiverPort = clientReceiverPort;

        datagramSocket = new DatagramSocket();
        serverManager.registerSocket(datagramSocket);

        // Each client session needs a different user
        this.userController = new UserController(databaseManager);
    }

    @Override
    public void run() {
        FancyLog.println("Started ClientSessionHandler thread.", FancyLog.Status.OK);


        //
        while (serverManager.getIsRunning()) {

            try {
                if (phase == Phase.PRE_LOGIN) {
                    // MAX 30 seconds to answer on pre-login
                    clientSocket.setSoTimeout(30_000);
                } else {
                    clientSocket.setSoTimeout(0);
                }
                Object receivedObject = SocketUtil.receiveWithExceptions(clientSocket);
                // huh
                if (receivedObject == null) {
                    FancyLog.println("Failed to receive data. Disconnecting.");
                    break;
                }

                // Sync to make sure we don't send heartbeats on the other thread while we are updating the db
                synchronized (QueryLogger.logLock) {
                    switch (receivedObject) {
                        // authentication
                        case RegisterInstructorRequest request -> {
                            RegisterUserResponse userResponse = userController.registerInstructor(request);
                            SocketUtil.send(clientSocket, userResponse);
                        }
                        case RegisterStudentRequest request -> {
                            RegisterUserResponse userResponse = userController.registerStudent(request);
                            SocketUtil.send(clientSocket, userResponse);
                        }
                        case LoginUserRequest request -> {
                            LoginUserResponse loginUserResponse = userController.loginUser(request);
                            if (userController.getActiveUser() != null) {
                                phase = Phase.LOGGED;
                            }
                            SocketUtil.send(clientSocket, loginUserResponse);
                        }
                        // auth edit
                        case EditInstructorRequest request -> {
                            EditUserResponse userResponse = userController.editInstructor(request);
                            SocketUtil.send(clientSocket, userResponse);
                        }
                        case EditStudentRequest request -> {
                            EditUserResponse userResponse = userController.editStudent(request);
                            SocketUtil.send(clientSocket, userResponse);
                        }

                        // instructor question management
                        case CreateQuestionRequest request -> {
                            CreateQuestionResponse response = questionController.createQuestion(request, userController);
                            SocketUtil.send(clientSocket, response);
                        }
                        case EditQuestionRequest request -> {
                            EditQuestionResponse response = questionController.editQuestion(request, userController);
                            SocketUtil.send(clientSocket, response);
                        }
                        case DeleteQuestionRequest request -> {
                            DeleteQuestionResponse response = questionController.deleteQuestion(request, userController);
                            SocketUtil.send(clientSocket, response);
                        }
                        case ListInstructorQuestionsRequest request -> {
                            ListInstructorQuestionsResponse questionsResponse =
                                    questionController.listInstructorQuestions(request, userController);
                            SocketUtil.send(clientSocket, questionsResponse);
                        }
                        case ViewQuestionStatisticsRequest request -> {
                            ViewQuestionStatisticsResponse response =
                                    questionController.viewQuestionStatistics(request, userController);
                            SocketUtil.send(clientSocket, response);
                        }
                        //Student question management
                        case ShowQuestionOptionsRequest request -> {
                            ShowQuestionOptionsResponse questionResponse = questionController.studentViewQuestionChoices(request, userController);
                            SocketUtil.send(clientSocket, questionResponse);
                        }
                        case SubmitQuestionAnswerRequest request -> {
                            SubmitQuestionAnswerResponse questionResponse = questionController.studentSubmitQuestionAnswer(request, userController);
                            SocketUtil.send(clientSocket, questionResponse);
                        }
                        case ShowQuestionsAnswerRequest request -> {
                            ShowQuestionsAnswerResponse questionResponse = questionController.studentShowQuestionAnswers(request, userController);
                            SocketUtil.send(clientSocket, questionResponse);
                        }
                        default -> {

                        }
                    }

                    // Replicate database updates
                    QueryLogger.sendLogs(datagramSocket, multicastSocketAddress, serverManager.getLocalAddress(), serverReceiverPort, clientReceiverPort);
                }


            } catch (ClassNotFoundException ex) {
                FancyLog.println("Received invalid packet.");
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            } catch (IOException ex) {
                FancyLog.println("Client connection lost.");
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }

        FancyLog.println("Closed ClientSessionHandler thread.");
    }


    enum Phase {
        PRE_LOGIN,
        LOGGED
    }
}




