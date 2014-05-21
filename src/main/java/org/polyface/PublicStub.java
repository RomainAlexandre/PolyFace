package org.polyface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PublicStub extends Remote{
	public void inviter(PublicStub stub) throws RemoteException;
	public String getDescription() throws RemoteException;
	public Wall accept(PublicStub stub, Wall wall) throws RemoteException;
}
