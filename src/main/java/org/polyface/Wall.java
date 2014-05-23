package org.polyface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Wall extends Remote {
    public List<String> visiterMur() throws RemoteException;
    public List<PublicStub> recupererListeAmis() throws RemoteException;
    public void notifierAmis(Wall monMur) throws RemoteException;
}
