package Client;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by robin on 2015-10-06.
 */
public interface Notifiable extends Remote {
   public void sendMessage(String msg) throws RemoteException;
   public boolean isAlive() throws RemoteException;
}
