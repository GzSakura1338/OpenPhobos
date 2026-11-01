package org.phobos.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BootstrapRemote extends Remote {
   void handleClientInitializer(ClientInitializer var1, long var2) throws RemoteException;
}
