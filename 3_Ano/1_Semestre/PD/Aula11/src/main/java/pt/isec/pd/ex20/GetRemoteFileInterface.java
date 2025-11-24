package pt.isec.pd.ex20;
 
/**
 *
 * @author Jose'
 */

public interface GetRemoteFileInterface extends java.rmi.Remote {
    byte[] getFileChunk(String fileName, long offset) throws  java.io.IOException;

    long getFileSize(String filename) throws java.io.IOException;
}
