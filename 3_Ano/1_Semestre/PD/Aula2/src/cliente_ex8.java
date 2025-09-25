import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;

public class cliente_ex8 { //cliente TCP

    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 60; //segundos

    public static void main(String[] args)
    {
        File localDirectory;
        String fileName, localFilePath = null;
        // FileOutputStream localFileOutputStream = null;
        int nChunks = 0;
        int receivedBytes = 0;

        if(args.length != 4){
            System.out.println("Sintaxe: java GetFileUdpClient serverAddress serverUdpPort fileToGet localDirectory");
            return;
        }

        fileName = args[2];
        localDirectory = new File(args[3]);

        if(!localDirectory.exists()){
            System.out.println("A directoria " + localDirectory + " nao existe!");
            return;
        }

        if(!localDirectory.isDirectory()){
            System.out.println("O caminho " + localDirectory + " nao se refere a uma directoria!");
            return;
        }

        if(!localDirectory.canWrite()){
            System.out.println("Sem permissoes de escrita na directoria " + localDirectory);
            return;
        }

        try{
            localFilePath = localDirectory.getCanonicalPath()+File.separator+fileName;
        }catch(IOException e){
            System.out.println("Ocorreu a excepcao {" + e +"} ao obter o caminho canonico para o ficheiro local!");
            return;
        }

        try(
              FileOutputStream localFileOutputStream = new FileOutputStream(localFilePath);
              Socket socket = new Socket(args[0],Integer.parseInt(args[1]));
              PrintStream pout = new PrintStream(socket.getOutputStream(), true);
        ){

            System.out.println("Ficheiro " + localFilePath + " criado.");

            socket.setSoTimeout(TIMEOUT*1000);
            pout.println(fileName);

            byte[] buff = new byte[MAX_SIZE];
            int nBytes;

            do{
                nBytes = socket.getInputStream().read(buff);//ler bytes ate echer(4001) ou msg total
                if(nBytes > 0){ //se for fechado vem imediatamente 0 senao a ligaçao ainda esta ativa

                    localFileOutputStream.write(buff, 0, nBytes);
                    ++nChunks;
                    receivedBytes++;
                }

            }while(nBytes > 0);

            System.out.println("Transferencia concluida.");

        }catch(UnknownHostException e){
            System.out.println("Destino desconhecido:\n\t"+e);
        }catch(NumberFormatException e){
            System.out.println("O porto do servidor deve ser um inteiro positivo:\n\t"+e);
        }catch(SocketTimeoutException e){
            System.out.println("Timeout de recepcao");
        }catch(SocketException e){
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t"+e);
        }catch(IOException e){
            System.out.println("Ocorreu um erro no acesso ao socket ou ao ficheiro local " + localFilePath +":\n\t"+e);
        }

        System.out.format("Foram recebidos %d blocos, incluindo o final vazio, " +
                "num total de %d bytes\r\n", nChunks, receivedBytes);

    }

}