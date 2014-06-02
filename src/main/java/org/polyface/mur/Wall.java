package org.polyface.mur;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import org.polyface.user.PublicStub;

public interface Wall extends Remote {
    public List<Message> visiterMur() throws RemoteException;
    public List<PublicStub> recupererListeAmis() throws RemoteException;
    public void notifierAmis(Wall monMur) throws RemoteException;
}
