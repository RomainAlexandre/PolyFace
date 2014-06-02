package org.polyface.user;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

import org.polyface.mur.Wall;
import org.polyface.mur.WallImpl;

public class PublicStubImpl extends UnicastRemoteObject implements PublicStub {
	private static final long serialVersionUID = -6633637570710002317L;

	private List<PublicStub> requetesEnAttente;
	private String monNom;
	private String maDescription;
	private WallImpl monMur;
	private List<Wall> mursAmis;

	public PublicStubImpl() throws RemoteException {
		super();
		monNom = "NoName";
		maDescription = "Inconnu";
		requetesEnAttente = new LinkedList<PublicStub>();
		mursAmis = new LinkedList<Wall>();
		monMur = new WallImpl();
	}

	public PublicStubImpl(String name, String description, int port)
			throws RemoteException {
		super(port);
		monNom = name;
		maDescription = description;
		requetesEnAttente = new LinkedList<PublicStub>();
		mursAmis = new LinkedList<Wall>();
		monMur = new WallImpl(port + 1);
	}

	public WallImpl getMonMur() {
		return monMur;
	}

	public List<PublicStub> getRequetesEnAttente() {
		return requetesEnAttente;
	}

	public List<Wall> getMursAmis() {
		return mursAmis;
	}

	@Override
	public boolean inviter(PublicStub stub) throws RemoteException {
		if (this.getMonMur().getListeAmis().contains(stub) || stub.getDescription().equals(this.getDescription()))
			return false;
		requetesEnAttente.add(stub);
		System.out.println("\n* Invitation de "+ stub.getName() + " reçue! *");
		return true;
	}

	@Override
	public String getDescription() throws RemoteException {
		return "Je suis " + monNom + " et voici ma description : "
				+ maDescription;
	}
	
	@Override
	public String getName() throws RemoteException {
		return this.monNom;
	}

	@Override
	public Wall accept(PublicStub stub, Wall wall) throws RemoteException {
		System.out.println("\n* Votre invitation a été acceptée par " + stub.getName() +" *");
		this.mursAmis.add(wall);
		this.monMur.getListeAmis().add(stub);

		return monMur;
	}

}
