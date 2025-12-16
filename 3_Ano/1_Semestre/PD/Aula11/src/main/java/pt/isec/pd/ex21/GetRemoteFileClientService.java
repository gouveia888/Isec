/*
 * Esta classe tambem implementa uma interface remota (GetRemoteFileClientInterface)
 * que deve incluir o metodo:
 *
 *       void writeFileChunk(byte [] fileChunk, int nbytes) throws java.io.IOException
 *
 */
 
package pt.isec.pd.ex21;

import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Jose'
 */
public class GetRemoteFileClientService //... {

    FileOutputStream fout = null;  
    
    //...

    public synchronized void setFout(FileOutputStream fout) {
        this.fout = fout;
    }
    
    //...
           
}
