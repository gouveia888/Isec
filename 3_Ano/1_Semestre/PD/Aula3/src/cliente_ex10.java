import java.net.*;
import java.io.*;
import java.util.*;
public class cliente_ex10 {

    public static final int MAX_SIZE = 4000;
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10; //segundos

    public static void main(String[] args) throws IOException {

        InetAddress serverAddr = null;
        int serverPort = -1;
        Calendar response;

        if(args.length != 2){
            System.out.println("Sintaxe: java TcpTimeClient serverAddress serverUdpPort");
            return;
        }

        try {
            serverAddr = InetAddress.getByName(args[0]);
            serverPort = Integer.parseInt(args[1]);

            try(Socket socket = new Socket(serverAddr, serverPort);
                ObjectInputStream in =new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                socket.setSoTimeout(TIMEOUT*1000);

                out.writeObject(TIME_REQUEST);
                out.flush();

                //A resposta deve terminar com uma mundanca de linha.
                //Os caracteres de mudanca de linha nao sao copiados para "response"
                response = (Calendar)in.readObject();

                System.out.println("Hora recebida do servidor: " +
                        response.get(Calendar.HOUR_OF_DAY)+":"+
                        response.get(Calendar.MINUTE)+":"+
                        response.get(Calendar.SECOND));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


        }catch(UnknownHostException e){
            System.out.println("Destino desconhecido:\n\t"+e);
        }catch(NumberFormatException e){
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        }catch(SocketTimeoutException e){
            System.out.println("Não foi recebida qualquer resposta:\n\t"+e);
        }catch(IOException e){
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
        }
    }

}