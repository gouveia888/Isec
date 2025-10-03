import java.util.*;
import java.net.*;
import java.io.*;
public class servidor_ex10 {
    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";
    public static final boolean debug = true;
    public static void main(String args[]){
        int listeningPort;
        String request, timeMsg;
        Calendar calendar;

        if(args.length != 1){
            System.out.println("Sintaxe: java TcpTimeServer listeningPort");
            return;
        }

        listeningPort = Integer.parseInt(args[0]);

        try(ServerSocket socket = new ServerSocket(listeningPort)){

            System.out.println("TCP Time Server iniciado no porto " + socket.getLocalPort() + " ...");

            while(true){

                try(Socket toClientSocket = socket.accept();){

                    try(ObjectOutputStream out = new ObjectOutputStream(toClientSocket.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(toClientSocket.getInputStream())){

                        request = (String)(in.readObject());
                        if(request==null){ //EOF
                            continue; //to next client request
                        }
                        if(debug){
                            System.out.println("Recebido \"" + request.trim() + "\" de " +
                                    toClientSocket.getInetAddress().getHostAddress() + ":" +
                                    toClientSocket.getPort());
                        }


                    if(!request.equalsIgnoreCase(TIME_REQUEST)){
                        System.out.println("Unexpected request");
                        continue;
                    }

                    //Constroi a resposta terminando-a com uma mudança de lina
                        calendar = GregorianCalendar.getInstance();

                        //Envia a resposta ao cliente
                        out.writeObject(calendar);
                        out.flush();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }catch(IOException e){
                    System.out.println("Erro na comunicacao com o cliente atual:\n\t" + e);
                }
            }

        }catch(NumberFormatException e){
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        }catch(IOException e){
            System.out.println("Ocorreu um erro ao nivel do socket de escuta:\n\t"+e);
        }
    }

}
