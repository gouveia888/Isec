package pt.isec.pd.ex19;

import java.rmi.server.ServerNotActiveException;
import java.util.List;

public interface RemoteTimeInterface extends java.rmi.Remote {
    public Hora getHora() throws java.rmi.RemoteException;
    public void putMsg(String msg) throws java.rmi.RemoteException, ServerNotActiveException;
    public String getMsg() throws java.rmi.RemoteException;
    public List<String> getMsgs() throws java.rmi.RemoteException;
}
