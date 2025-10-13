/**
 * @author José Marinho
 */

package pt.isec.pd.ex15a;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.sql.*;

public class DistributedPiMasterComSerializacao {

    static final int TIMEOUT = 60000; //60 seconds

    static final String GET_WORKERS_QUERY = "SELECT * FROM pi_workers;";

    private static int getWorkers(String sgbdAddress, String bdName, String user, String pass, List<Socket> workers) {

        String workerAdress;
        int workerPort;
        Socket socketToWorker;

        workers.clear();
        
        String dbUrl = "jdbc:mysql://"+sgbdAddress+"/"+bdName;

        try(
                //establece ligacao a base de dados
            Connection conn = DriverManager.getConnection(dbUrl,user,pass); // Connect the database using dbUrl, user, and pass attributes
            Statement stmt = conn.createStatement() //objeto para enviar queries SQL
        ) {

            ResultSet rs = stmt.executeQuery("Select CURRENT_TIMESTAMP");
            Timestamp currentTimestampInServer = rs.next() ? rs.getTimestamp("current_timestamp"):null;

            rs = stmt.executeQuery(GET_WORKERS_QUERY);

            while (rs.next()) {

                try {

                    workerAdress = rs.getString("address");
                    workerPort = rs.getInt("port");
                    Timestamp timestamp = rs.getTimestamp("timestamp");

                    System.out.println("> DB entry: [" + workerAdress + ":" + workerPort + "]");
                    
                    long elapsedTime = currentTimestampInServer.getTime() - timestamp.getTime(); //verifica se o worker atualizou a sua entrada recentemente
                    System.out.println("\t... Entry created/updated " + elapsedTime/1000 + " seconds ago");
                    
                    if(elapsedTime > 2.5 * 10000){ //se a entrada nao foi atualizada recentemente, considera-se que o worker ja nao esta' disponivel trocar pela DistributedPiWorkerComSerializacao.DB_UPDATE_DELAY
                        System.out.println("\t... Entry will be deleted!");
                        try(Statement stmt2 = conn.createStatement()){ //Se usarmos stmt, rs e' encerrado, o que resulta numa excepcao no while
                            stmt2.executeUpdate("DELETE FROM pi_workers WHERE address = '" + workerAdress + "' AND port = " + workerPort + ";");
                        }
                        continue;
                    }

                    System.out.print("> Connecting to worker " + (workers.size() + 1));
                    System.out.println(" [" + workerAdress + ":" + workerPort + "]... ");
                    
                    socketToWorker = new Socket(workerAdress, workerPort); // Open a TCP connection to the worker
                    socketToWorker.setSoTimeout(TIMEOUT); // Define um timeout de TIMEOUT ms para operações de leitura no socket
                    workers.add(socketToWorker);

                    System.out.println("\t... connection established!");                    
                    
                }catch (IOException e) {
                    System.out.println("\r\n> Cannot connect to host!\r\n\t " + e + "\r\n");
                }

            } //while

        } catch (SQLException ex) {            
            System.out.println(ex);
        } 

        return workers.size();
    }

    public static void main(String[] args) throws InterruptedException {
        long nIntervals;

        List<Socket> workers = new ArrayList<>();
        ObjectOutputStream output;
        ObjectInputStream input;

        int i, nWorkers;
        double workerResult;
        double pi = 0;

        Calendar t1, t2;

        System.out.println();

        if (args.length != 5) {
            System.out.println("> Syntax: java ParallelPi <number of intrevals> "
                    + "<SGBD address> <BD name> <usename> <password>");
            return;
        }

        nIntervals = Long.parseLong(args[0]);

        t1 = GregorianCalendar.getInstance();
        nWorkers = getWorkers(args[1], args[2], args[3], args[4], workers);
        //array list de workers e estabelece ligacoes TCP a cada worker
        if (nWorkers <= 0) {
            return;
        }

        try {

            for (i = 0; i < nWorkers; i++) {
                output =  new ObjectOutputStream(workers.get(i).getOutputStream()); // Create an ObjectOuputStream to transmit objects to worker at index i
                output.writeObject(new RequestToWorker(i + 1, nWorkers, nIntervals)); //Send the request serialized to the worker
                output.flush();
            }

            System.out.println();

            for (i = 0; i < nWorkers; i++) {
                input = new ObjectInputStream(workers.get(i).getInputStream()); // Create an ObjectInputStream to receive object from worker at index i
                workerResult = (Double)input.readObject(); // Get the result from worker at index i
                System.out.println("> Worker " + (i + 1) + ": " + workerResult);
                pi += workerResult;
            }

        } catch (IOException e) {
            System.err.println("> Erro ao aceder ao socket\r\n\t" + e);
        } catch (ClassNotFoundException e) {
            System.err.println("> Recebido objecto de tipo inesperado\r\n\t" + e);
        } finally {
            /* Close all the sockets in workers List */
            for(Socket w : workers){
                try {
                    w.close();
                }catch (IOException ex) {}
            }
            workers.clear();
        }

        t2 = GregorianCalendar.getInstance();

        System.out.println();
        System.out.println("> Valor aproximado do pi: " + pi + " (calculado em "
                + (t2.getTimeInMillis() - t1.getTimeInMillis()) + " msec.)");
        
    }
}
