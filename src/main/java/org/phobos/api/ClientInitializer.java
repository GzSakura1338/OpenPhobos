package org.phobos.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInitializer extends Remote {
   void handleLong(long... var1) throws RemoteException;
}
