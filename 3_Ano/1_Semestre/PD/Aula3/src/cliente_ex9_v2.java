import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class cliente_ex9_v2 { //UDP
    public static final int MAX_SIZE = 5000;
    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10; //segundos

    public static void main(String[] args) throws IOException {

        InetAddress serverAddr = null;
        DatagramPacket packet = null;
        int serverPort;
        Calendar response;

        if(args.length != 2){
            System.out.println("Sintaxe: java UdpTimeClient serverAddress serverUdpPort");
            return;
        }

        try(DatagramSocket socket = new DatagramSocket()){

            serverPort = Integer.parseInt(args[1]);

            serverAddr = InetAddress.getByName(args[0]);
            socket.setSoTimeout(TIMEOUT*1000);

            try(ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bOut)){

                out.writeObject(TIME_REQUEST);
                out.flush();

                packet = new DatagramPacket(bOut.toByteArray(), bOut.size(), serverAddr,
                        serverPort);
            }
            socket.send(packet);

            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE); //a mensagem pode ter um tamanho diferente
            socket.receive(packet);

            try(ByteArrayInputStream bIn = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                ObjectInputStream in = new ObjectInputStream(bIn)) {

                response = (Calendar)in.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Hora indicada pelo servidor: " + response.getTime());
            System.out.println();
            System.out.println("Horas: " + response.get(GregorianCalendar.HOUR_OF_DAY) +
                    " \nMinutos: " + response.get(GregorianCalendar.MINUTE) +
                    " \nSegundos: " + response.get(GregorianCalendar.SECOND));
            //******************************************************************

        }catch(UnknownHostException e){
            System.out.println("Destino desconhecido:\n\t"+e);
        }catch(NumberFormatException e){
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        }catch(SocketTimeoutException e){
            System.out.println("Nao foi recebida qualquer resposta:\n\t"+e);
        }catch(SocketException e){
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t"+e);
        }catch(IOException e){
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
        }
    }

}
