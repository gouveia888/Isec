/*Ex. 12: neste exercicio, a informacao e' trocada via um objecto
serializado que encapsula o "nickname" e a mensagem.*/

/*
   Multicast IPv4 em Mac OS
   Na lina de comando: -Djava.net.preferIPv4Stack=true
   No codigo: System.setProperty("java.net.preferIPv4Stack", "true");
*/
//package Ex12;
package pt.isec.pd.aula5.multicast;
import java.io.*;
import java.net.*;
import java.util.Enumeration;
//import java.util.Enumeration;

class Msg implements Serializable
{	
	protected String nickname;
	protected String msg;
    private static final long serialVersionUID = 1010L;
	
	public Msg(String nickname, String msg){
		this.nickname = nickname;
		this.msg = msg;
	}
	
	public String getNickname(){ return nickname; }
	public String getMsg(){ return msg; }
        
}

public class MulticastChat_v2 extends Thread
{
    public static final String LIST = "LIST";
    public static String EXIT = "EXIT";
    public static int MAX_SIZE = 1000;
    
    protected String username;
    protected MulticastSocket s;
    protected boolean running;

    public MulticastChat_v2(String username, MulticastSocket s){
        this.username = username;
        this.s = s;
        running = true;
    }
    
    public void terminate(){
        running = false;
    }
            
    @Override
    public void run() {
       
        DatagramPacket pkt;
        Msg msg;
        Object obj;
        
        if(s == null || !running){
            return;
        }
        
        try{
            
            while(running){
                
                pkt = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                s.receive(pkt);
                
                try(ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(pkt.getData(),0,pkt.getLength()));){
                    obj = in.readObject();
                    // "Deserializa" o objecto transportado no datagrama acabado de ser recebido

                    
                    System.out.println();
                    System.out.print("(" + pkt.getAddress().getHostAddress() + ":" + pkt.getPort() + ") ");
                    
                    //Caso o objecto recebido seja uma instancia de Msg...
                    if(obj instanceof Msg){
                        
                        msg = (Msg)obj;
                        
                        if(msg.getMsg().toUpperCase().contains(LIST.toUpperCase())){
                            
                            //Envia o username 'a origem sob a forma de um objecto serializado do tipo String
                            try(ByteArrayOutputStream buff = new ByteArrayOutputStream();
                            ObjectOutputStream out = new ObjectOutputStream(buff);){

                                out.writeObject(username);
                                out.flush();
                                pkt.setData(buff.toByteArray());
                                pkt.setLength(buff.size());
                            }
                            s.send(pkt);
                            continue;
                        }
                        
                        //Mostra a mensagem recebida bem como a identificacao do emissor
                        System.out.println("Recebido \"" + pkt.getAddress().getHostAddress() + ":" + pkt.getPort() + "\" de " + msg.getNickname() + ": " + msg.getMsg()+ "\n" + msg.getClass());
                         
                    //Caso o objecto recebido seja uma instancia de String...
                    } else if(obj instanceof String){
                        
                        //Mostra a String
                        System.out.println((String)obj + obj.getClass());
                    }
                    
                    System.out.println(); System.out.print("> ");
                                          
                }catch(ClassNotFoundException e){
                    System.out.println();
                    System.out.println("Mensagem recebida de tipo inesperado!");
                }catch(IOException e){
                    System.out.println();
                    System.out.println("Impossibilidade de aceder ao conteudo da mensagem recebida!");
                }
                
            }
            
        }catch(IOException e){
            if(running){
                System.out.println(e);
            }
            
            if(!s.isClosed()){                
                s.close();
            }
        }   
        
    }

    public static void showNetworkInterfaces() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while(interfaces.hasMoreElements()){
            NetworkInterface interf = interfaces.nextElement();
            try{
                Enumeration<InetAddress> addresses = interf.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    String address = addresses.nextElement().getHostAddress();
                    if (!address.contains(":"))
                        System.out.println(interf.getName() + ": " + address);
                }
            }catch(Exception ex){
                System.out.println();
            }
        }
    }
    
    public static void main(String[] args) throws UnknownHostException, IOException {
     
        InetAddress group;
        int port;
        MulticastSocket socket = null;
        DatagramPacket dgram;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String msg;
        NetworkInterface nif;
        
        MulticastChat_v2 t = null;
        
        if(args.length != 4){
            System.out.println("Sintaxe: java MulticastChat <nickname> <groupo multicast> <porto> <interface de rede usada para multicast>");
            return;
        }
        
        try{
            group = InetAddress.getByName(args[1]);
            port = Integer.parseInt(args[2]);

            showNetworkInterfaces();

            try{                
                nif = NetworkInterface.getByInetAddress(InetAddress.getByName(args[3])); //e.g., 127.0.0.1, 192.168.10.1, ... 
            }catch (SocketException | NullPointerException | UnknownHostException | SecurityException ex){               
                nif = NetworkInterface.getByName(args[3]); //e.g., lo0, eth0, wlan0, en0, ...
            }
                        
            socket = new MulticastSocket(port);
            socket.joinGroup(new InetSocketAddress(group, port), nif);

            //Lanca a thread adicional dedicada a aguardar por datagramas no socket e a processá-los
            t = new MulticastChat_v2(args[0], socket);
            //t.setDaemon(true);
            t.start();
            
            System.out.print("> ");
            
            while(true){              
                
                msg = in.readLine();
                
                if(msg.equalsIgnoreCase(EXIT)){
                    break;
                }
                
                try(ByteArrayOutputStream buff = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(buff);){

                    out.writeObject(new Msg(args[0], msg));
                    out.flush();
                    dgram = new DatagramPacket(buff.toByteArray(), buff.size(), group, port);

                }

                socket.send(dgram);   
                
            }
            
        }finally{
            if(t != null){
                t.terminate();
            }
            
            if(socket != null){
                socket.close();
            }

            //t.join(); //Para esperar que a thread termine caso esteja em modo daemon
        }
    }
}
