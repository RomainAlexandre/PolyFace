package org.polyface;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;

public class User{
	
	private SmartProxy monPublicStub;
	private Registry reg;
	private String name;
    
    public User() throws RemoteException{
        try {
    		reg = LocateRegistry.createRegistry(2001);
    	} catch (ExportException ee) {
    		reg = LocateRegistry.getRegistry(2001);
    	}
        monPublicStub = new SmartProxy();
        this.name = "NoName";
    	System.out.println(name + " : Je m'enregistre dans le registre");

        enregistrerPublicStub();
    }

    public User(String name, String description, int port) throws RemoteException{
        try {
    		reg = LocateRegistry.createRegistry(2001);
    	} catch (ExportException ee) {
    		reg = LocateRegistry.getRegistry(2001);
    	}
        monPublicStub = new SmartProxy(name, description, port);
        this.name = name;
    	System.out.println(name + " : Je m'enregistre dans le registre");

        enregistrerPublicStub();
    }
    
	public PublicStubImpl getMonPublicStub() {
		return monPublicStub.getPublicStubImpl();
	}

	public WallImpl getMonMur() {
		return monPublicStub.getPublicStubImpl().getMonMur();
	}
	
    private void enregistrerPublicStub() throws AccessException, RemoteException{
		reg.rebind("rmi://localhost:2001/Facebook"+name, (PublicStub) monPublicStub);
    }
    
    public PublicStub findPublicStub(String name) throws AccessException, RemoteException, NotBoundException{
    	return (PublicStub) reg.lookup("rmi://localhost:2001/Facebook"+name);
    }
    
    public void ecrireSurMonMur(String s) throws RemoteException{
    	this.getMonMur().ajouterContenuMur(s);
    	for(Wall w : this.monPublicStub.getPublicStubImpl().getMursAmis()){
    		w.notifierAmis(this.getMonMur());
    	}
    }
    
    public void accepter(int numeroPos) throws RemoteException {
    	PublicStub p = this.monPublicStub.getPublicStubImpl().getRequetesEnAttente().remove(numeroPos);
    	
    	Wall w = p.accept(getMonPublicStub(), getMonMur());
    	
    	this.monPublicStub.getPublicStubImpl().getMursAmis().add(w);
    	this.getMonMur().getListeAmis().add(p);
    }

	public void afficherAmis() throws RemoteException {
		System.out.println(name + " : Mes amis sont : ");
		for(PublicStub p : this.getMonMur().getListeAmis()){
			System.out.println(p.getDescription());
		}
	}

	public void visiterMur(int i) throws RemoteException {
		Wall w = this.getMonMur().getMurAVisiter().get(0);
    	
		System.out.println(name + " : J'affiche un mur :");
    	for(String s : w.visiterMur()){
    		System.out.println(s);
    	}
	}
}
