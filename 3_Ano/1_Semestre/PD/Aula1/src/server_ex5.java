import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class server_ex5 {

    // UDP PORT TO WHICH SERVICE IS BOUND
    public static final int SERVICE_PORT = 7000;
    // MAX SIZE OF PACKET, LARGE ENOUGH FOR ALMOST ANY CLIENT
    public static final int BUFSIZE = 4096;
    // SOCKET USED FOR READING AND WRITING UDP PACKETS
    // SOCKET USED FOR READING AND WRITING UDP PACKETS
    private DatagramSocket socket = null;

    public static void main(String args[])
    {
        server_ex5 server = new server_ex5();
        server.serviceClients();
    }

    public server_ex5() //constructor
    {
        try
        {
            // BIND TO THE SPECIFIED UDP PORT
            socket = new DatagramSocket( SERVICE_PORT );
            System.out.println("Server active on port "+socket.getLocalPort());
        }catch (Exception e){
            System.err.println ("Unable to bind port");
            e.printStackTrace();
        }
    }

    public void serviceClients()
    {
        if(socket == null) return;
        // CREATE A BUFFER LARGE ENOUGH FOR INCOMING PACKETS
        byte[] buffer = new byte[BUFSIZE];

        while(true){
            try {
                // CREATE A DATAGRAMPACKET FOR READING UDP PACKETS
                DatagramPacket receivePacket = new DatagramPacket( buffer, BUFSIZE );
                // RECEIVE INCOMING PACKETS
                socket.receive(receivePacket);

                // Obter a data e hora atuais
                LocalDateTime agora = LocalDateTime.now();

                // Formatar a data e hora para uma string legível
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String horaAtual = agora.format(formatter);

                // Converter a string para um array de bytes
                byte[] responseData = horaAtual.getBytes();

                // usar o mesmo packet alterar  tamanho da mensagem
                receivePacket.setData(responseData);
                receivePacket.setLength(responseData.length);


                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length,
                        receivePacket.getAddress(), receivePacket.getPort());

                //socket.send(responsePacket);
                socket.send(receivePacket);

                System.out.println("Packet received from " + receivePacket.getAddress()
                        + ":" + receivePacket.getPort() + " of length "
                        + receivePacket.getLength());
                // ECHO THE PACKET BACK - ADDRESS AND PORT ARE ALREADY SET!
            }catch (IOException e){
                System.err.println ("Error : " + e);
            }
        } // while
    } // serviceClientes() method
}
