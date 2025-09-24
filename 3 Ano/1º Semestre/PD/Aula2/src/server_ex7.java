import java.io.*;
import java.net.*;
import java.util.Calendar;

/**
 *
 * @author Jose' Marinho
 */
public class server_ex7 {

    public static final int MAX_SIZE = 4000;
    public static final String TIME_REQUEST = "TIME";

    public static void main(String[] args){

        File localDirectory;
        String requestedFileName, requestedCanonicalFilePath = null;
        String receiveMsg, timeMsg;
        Calendar calendar = Calendar.getInstance();
        int listeningPort;
        DatagramPacket packet;

        byte []fileChunk = new byte[MAX_SIZE];

        try(ServerSocket socket = new ServerSocket(Integer.parseInt(args[0]))) {

            System.out.println("Servidor iniciado...");

            while(true) {

                try(
                        Socket cli = socket.accept();
                        BufferedReader bin = new BufferedReader(new InputStreamReader(cli.getInputStream()));
                        PrintWriter pw = new PrintWriter(cli.getOutputStream());
                   ){
                            if((receiveMsg = bin.readLine()) == null)
                                continue;

                            System.out.println("Recebido \"" + receiveMsg + "\" de " + cli.getInetAddress().getHostAddress()+":"+cli.getPort());

                            if(!receiveMsg.equalsIgnoreCase(TIME_REQUEST))
                                continue;

                            calendar = Calendar.getInstance();
                            timeMsg = String.format("%02d:%02d:%02d",calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));

                            pw.println(timeMsg);
                }//try catch

            } //while

        }catch(NumberFormatException e){
            System.out.println("O porto de escuta deve ser um inteiro positivo:\n\t"+e);
        }catch(SocketException e){
            System.out.println("Ocorreu uma excepcao ao nivel do socket UDP:\n\t"+e);
        }catch(FileNotFoundException e){   //Subclasse de IOException
            System.out.println("Ocorreu a excepcao {" + e + "} ao tentar abrir o ficheiro " + requestedCanonicalFilePath + "!");
        }catch(IOException e){
            System.out.println("Ocorreu a excepcao de E/S: \n\t" + e);
        }

    } // main
}