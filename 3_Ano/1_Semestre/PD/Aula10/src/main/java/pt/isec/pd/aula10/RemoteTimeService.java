package pt.isec.pd.aula10;

import pt.isec.pd.ex19.Hora;
import pt.isec.pd.ex19.RemoteTimeInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class RemoteTimeService extends UnicastRemoteObject implements RemoteTimeInterface {

    List<String> messages = new ArrayList<>();

    RemoteTimeService() throws RemoteException {

    }

    @Override
    public Hora getHora() throws RemoteException {

        try{
            System.out.println("Mais uma invocaçao... (" + getClientHost() + ") tratada pela thead " + Thread.currentThread().getName());
        }catch (ServerNotActiveException e){
            System.out.println("Mais uma invocaçao... (host desconhecido) tratada pela thead " + Thread.currentThread().getName());
        }


        Calendar now = Calendar.getInstance();
        int h = now.get(java.util.Calendar.HOUR_OF_DAY);
        int m = now.get(java.util.Calendar.MINUTE);
        int s = now.get(java.util.Calendar.SECOND);

        return new Hora(h, m, s);
    }

    @Override
    public synchronized void putMsg(String msg) throws RemoteException, ServerNotActiveException {
        messages.add("("+ getClientHost() +")" + msg);
    }

    @Override
    public synchronized String getMsg() throws RemoteException {
        if(messages.isEmpty())
            return null;
        return messages.get(messages.size() - 1);
    }

    @Override
    public synchronized List<String> getMsgs(){
        return messages;
    }

    static public void main(String[] args){

        if(args.length > 0){
            System.setProperty("java.rmi.server.hostname",args[0]);
        }

        try{
            try{
                LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                System.out.println("RMI registry pronto.");
            }catch (Exception e){
                e.printStackTrace();
            }

            RemoteTimeInterface timeService = new RemoteTimeService();

            System.out.println("Servidor Remote Time criado.");

            Naming.bind("rmi://localhost/timeserver", timeService);

            System.out.println("Servidor Remote Time registado no regestry local com .");
            System.out.println("Hora local " + timeService.getHora());

            timeService.putMsg("Tiago Filipe");

            System.out.println("Serv msg");

            List<String> msgs = timeService.getMsgs();
            for(String msg : msgs){
                System.out.println(msg);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
