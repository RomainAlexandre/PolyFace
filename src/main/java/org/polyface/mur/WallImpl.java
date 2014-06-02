package org.polyface.mur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

import org.polyface.user.PublicStub;

public class WallImpl extends UnicastRemoteObject implements Wall {
	private static final long serialVersionUID = 637430402234320754L;
	
	private List<PublicStub> listeAmis;
	private List<Wall> murAVisiter;
    private List<Message> contenuMur;
    
	public WallImpl() throws RemoteException{
        super();
        contenuMur = new LinkedList<Message>();
        listeAmis = new LinkedList<PublicStub>();
        murAVisiter = new LinkedList<Wall>();
    }

    public WallImpl(int port) throws RemoteException{
        super(port);
        contenuMur = new LinkedList<Message>();
        listeAmis = new LinkedList<PublicStub>();
        murAVisiter = new LinkedList<Wall>();
    }
	
    public List<PublicStub> getListeAmis(){
    	return listeAmis;
    }
    
    public List<Wall> getMurAVisiter(){
    	return murAVisiter;
    }
    
    public void ajouterContenuMur(Message s){
    	this.contenuMur.add(s);
    }
    
    @Override
    public List<Message> visiterMur()  throws RemoteException{
        return contenuMur;
    }

    @Override
    public List<PublicStub> recupererListeAmis() throws RemoteException {
        return listeAmis;
    }

	@Override
	public void notifierAmis(Wall monMur) throws RemoteException {
		System.out.println("* Nouveau message posté! *");
		if(!murAVisiter.contains(monMur)){
			murAVisiter.add(monMur);
		}
	}

}
