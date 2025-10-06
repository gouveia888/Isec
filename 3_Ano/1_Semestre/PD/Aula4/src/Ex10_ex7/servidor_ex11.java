package Ex10_ex7;

import java.util.*;
import java.net.*;
import java.io.*;

//TCP

class ProcessClientThread extends Thread{ //podemos usar o implements Runnable
    private Socket toClientSocket;

    public ProcessClientThread(Socket toClientSocket){
        this.toClientSocket = toClientSocket;
    }

    @Override
    public void run() {
        String request,timeMsg;
        Calendar calendar;

        try(
            PrintWriter out = new PrintWriter(toClientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(toClientSocket.getInputStream()))
        ){

            toClientSocket.setSoTimeout(1000);
            request = in.readLine();

            if(request == null){ //EOF
               return;
            }

            System.out.println("Recebido \"" + request.trim() + "\" de " +
                    toClientSocket.getInetAddress().getHostAddress() + ":" +
                    toClientSocket.getPort());

            if(!request.equalsIgnoreCase(servidor_ex11.TIME_REQUEST)){
                System.out.println("Unexpected request");
                return;//termina a thread
            }

            //Constroi a resposta terminando-a com uma mudança de lina
            calendar = GregorianCalendar.getInstance();
            timeMsg = calendar.get(GregorianCalendar.HOUR_OF_DAY)+":"+
                    calendar.get(GregorianCalendar.MINUTE)+":"+
                    calendar.get(GregorianCalendar.SECOND);

            //Envia a resposta ao cliente
            out.println(timeMsg);
            out.flush();

            System.out.println("Sou a Thread" + Thread.currentThread().getName() + "\nEnviado:" + timeMsg + "\" a " +
                    toClientSocket.getInetAddress().getHostAddress() + ":" +
                    toClientSocket.getPort());

        }catch(IOException e){
            System.out.println("Erro na comunicacao com o cliente atual:\n\t" + e);
        }finally{
            try{
                toClientSocket.close();
            }catch(IOException e){
                System.out.println("Erro ao fechar o socket do cliente:\n\t" + e);
            }
        }
    }
}

public class servidor_ex11 {
    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";

    public static void main(String args[]){
        int listeningPort;

        if(args.length != 1){
            System.out.println("Sintaxe: java TcpTimeServer listeningPort");
            return;
        }

        listeningPort = Integer.parseInt(args[0]);

        try(ServerSocket socket = new ServerSocket(listeningPort)){

            System.out.println("TCP Time Server iniciado no porto " + socket.getLocalPort() + " ...");

            while(true){

                    Socket toClientSocket = socket.accept();
                    new ProcessClientThread(toClientSocket).start();
                    //new Thread(new Ex11.ProcessClientThread(toClientSocket),"name").start();
            }

        }catch(NumberFormatException e){
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        }catch(IOException e){
            System.out.println("Ocorreu um erro ao nivel do socket de escuta:\n\t"+e);
        }
    }

}
