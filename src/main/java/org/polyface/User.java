package org.polyface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class User extends UnicastRemoteObject implements PublicStub, Wall{
	private static final long serialVersionUID = 1L;
	
	private List<PublicStub> requetesEnAttente;
    private List<Wall> mursAmis;
    private List<PublicStub> listeAmis;
    private String maDescription;
    private String contenuMur;

    public User() throws RemoteException{
        super();
        maDescription = "Inconnu";
        contenuMur = "";
        requetesEnAttente = new LinkedList<PublicStub>();
        mursAmis = new LinkedList<Wall>();
        listeAmis = new LinkedList<PublicStub>();
    }

    public User(String description, int port) throws RemoteException{
        super(port);
        maDescription = description;
        contenuMur = "";
        requetesEnAttente = new LinkedList<PublicStub>();
        mursAmis = new LinkedList<Wall>();
        listeAmis = new LinkedList<PublicStub>();
    }
    
    public List<PublicStub> getRequetesEnAttente() {
		return requetesEnAttente;
	}
	
    @Override
    public void inviter(PublicStub stub) throws RemoteException {
        requetesEnAttente.add(stub);
        System.out.println("J'ai ete invite");
        stub.accept(this, this);
    }

    @Override
    public String getDescription() throws RemoteException {
        return maDescription;
    }
    
    @Override
    public Wall accept(PublicStub stub, Wall wall) throws RemoteException {
    	System.out.println("J'accepte");
    	this.mursAmis.add(wall);
    	this.listeAmis.add(stub);
    	
        return this;
    }
    

    @Override
    public String visiterMur()  throws RemoteException{
        return contenuMur;
    }

    @Override
    public List<PublicStub> recupererListeAmis() throws RemoteException {
        return listeAmis;
    }
    
}
