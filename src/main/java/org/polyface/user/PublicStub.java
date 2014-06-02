package org.polyface.user;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import org.polyface.mur.Wall;

public interface PublicStub extends Remote, Serializable{
	public boolean inviter(PublicStub stub) throws RemoteException;
	public String getDescription() throws RemoteException;
	public Wall accept(PublicStub stub, Wall wall) throws RemoteException;
}
