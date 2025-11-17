package pt.isec.pd.aula10;

import pt.isec.pd.ex19.RemoteTimeInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Aula10Ex19Client {
//EX 19 Cliente RMI que obtém a hora remota
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        //ip: 10.65.151.234
        try{
            String registry = "localhost";
            if (args.length >=1){ registry = args[0]; }
            String registration = "rmi://" + registry + "/timeserver";

            RemoteTimeInterface timeService = (RemoteTimeInterface)Naming.lookup(registration);

            System.out.println("Hora remota: " + timeService.getHora()); // imprime a hora remota

            timeService.putMsg("Tiago Filipe");


        }catch (RemoteException e){
            System.out.println("Erro remoto: " + e.getMessage());
        }catch (NotBoundException e){
            System.out.println("Servico remoto desconhecido: " + e.getMessage());
        }catch (MalformedURLException e){
            System.out.println("Erro: " + e.getMessage());
        }catch (Exception e){
            System.out.println("Erro geral: " + e.getMessage());
        }

    }

}
