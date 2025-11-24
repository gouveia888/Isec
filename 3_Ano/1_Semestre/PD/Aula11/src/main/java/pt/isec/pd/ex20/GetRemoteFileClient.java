/*
 * Exemplo de utilizacao do servico com interface remota GetRemoteFileInterface.
 * Assume-se que o servico encontra-se registado sob o nome "servidor-ficheiros-pd".
 */

package pt.isec.pd.ex20;

import java.io.*;
import java.rmi.*;

/**
 *
 * @author Jose'
 */
public class GetRemoteFileClient {

    public static void main(String[] args) {
                  
        File localDirectory;
        String fileName;                
        
        String localFilePath;
                
        byte [] b;    
        long offset;
        
        /*
         * Trata os argumentos da linha de comando 
         */        
        
        if(args.length != 3){
            System.out.print("Deve passar na linha de comando: (1) a localizacao do registry onde se encontra registado ");
            System.out.println("o servico com nome \"servidor-ficheiros-pd\"; (2) a directoria local ");
            System.out.println("onde pretende guardar o ficheiro obtido; e (3) o nome do ficheiro pretendido!");
            return;
        }        
                
        localDirectory = new File(args[1]);
        fileName = args[2];
                
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
            localFilePath = new File(localDirectory.getPath()+File.separator+fileName).getCanonicalPath();
        }catch(IOException ex){
            System.out.println(ex);
            return;
        }

        try(FileOutputStream localFileOutputStream = new FileOutputStream(localFilePath)){

            System.out.println("Ficheiro " + localFilePath + " criado.");
            
            /*
             * Obtem a referencia remota para o servico com nome "servidor-ficheiros-pd"
             */
            String registration = "rmi://" + args[0] + "/servidor-ficheiros-pd";
            GetRemoteFileInterface fileService = (GetRemoteFileInterface) Naming.lookup(registration); //explique o que o lookup faz internamente? EXAME


//            System.out.println("Tamanho do ficheiro:" + fileService.getFileSize(registration));
            /*
             * Obtem e guarda localmente os varios blocos do ficheiro pretendido.
             * Para o efeito, invoca o metodo getFileChunk no servico remoto para obter cada bloco.
             */
            offset = 0;

            while((b = fileService.getFileChunk(fileName,offset))!=null){
                localFileOutputStream.write(b);
                offset += b.length;
            }
            
            System.out.println("Transferencia do ficheiro " + fileName + " concluida.");
            
        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
        }catch(NotBoundException e){
            System.out.println("Servico remoto desconhecido - " + e);
        }catch(IOException e){
            System.out.println("Erro E/S - " + e);
        }catch(Exception e){
            System.out.println("Erro - " + e);
        }               
    }
}
