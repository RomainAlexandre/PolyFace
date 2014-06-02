package org.polyface.user;

import java.rmi.RemoteException;
import java.util.List;

import org.polyface.mur.Message;
import org.polyface.mur.Wall;
import org.polyface.mur.WallImpl;

public class User{
	
	private SmartProxy monPublicStub;
	private String name;
    
    public User() throws RemoteException{
        monPublicStub = new SmartProxy();
        this.name = "NoName";
    	System.out.println(name + " : Je m'enregistre dans le registre");
    }

    public User(String name, String description, int port) throws RemoteException{
        monPublicStub = new SmartProxy(name, description, port);
        this.name = name;
    }
    
	public SmartProxy getMonPublicStub() {
		return monPublicStub;
	}

	public WallImpl getMonMur() {
		return monPublicStub.getPublicStubImpl().getMonMur();
	}
    
	public String getName(){
		return this.name;
	}
	
    public void ecrireSurMonMur(String s) throws RemoteException{
    	this.getMonMur().ajouterContenuMur(new Message(s, name));
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

	public List<Message> getMessagesMur() throws RemoteException{
		return this.getMonMur().visiterMur();
	}
}
