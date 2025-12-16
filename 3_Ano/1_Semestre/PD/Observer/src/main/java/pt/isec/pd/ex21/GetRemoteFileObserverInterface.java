package pt.isec.pd.ex21;

import java.rmi.RemoteException;

public interface GetRemoteFileObserverInterface {
    void notifyNewOperationConcluded(String description) throws RemoteException;
}
