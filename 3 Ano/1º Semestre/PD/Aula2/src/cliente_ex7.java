import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;

//TCP so permite comunicaçaoa ponto a ponto

public class cliente_ex7 {
    public static final int MAX_SIZE = 256;
    public static final String TIME_REQUEST = "TIME";
    public  static final int TIMEOUT = 10;

    public static void main(String[] args) {
        InetAddress serverAddress = null; // a class InetAdres encapsula um IP
        String response;

        if(args.length != 2){
            System.out.println("Usage : java ex5.ex5 <server_address> <server_port>");
            System.exit(1);
        }

        try(
                Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
                PrintStream pout = new PrintStream(socket.getOutputStream(),true); //se colocar System.out escreve para o ecra
                BufferedReader bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));//BufferedReader nao te um construtor que receba um socket

            ){

            socket.setSoTimeout(TIMEOUT*10000);

            pout.println(TIME_REQUEST);
            //pout.flush();

            response = bin.readLine();

            if(response==null){
                System.out.println("O servidor nao enviou qualquer resposta antes de fechar a ligaçao TCP!");
            }

            System.out.format("Hora indicada pelo servidor (%s:%d): %s",socket.getInetAddress().getHostAddress(), socket.getPort(),response);
            //socket.getLocalPort() indica o porto local do meu socket
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