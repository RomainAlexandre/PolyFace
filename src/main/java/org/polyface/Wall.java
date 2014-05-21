package org.polyface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Wall extends Remote {
    public String visiterMur() throws RemoteException;
    public List<PublicStub> recupererListeAmis() throws RemoteException;
}
