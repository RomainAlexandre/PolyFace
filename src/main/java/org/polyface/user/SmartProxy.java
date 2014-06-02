package org.polyface.user;

import java.rmi.RemoteException;
import java.util.List;

import org.polyface.mur.Wall;

public class SmartProxy implements PublicStub {
	private static final long serialVersionUID = -744929499781069541L;
	
	private String monNom;
    private String maDescription;
    
    private PublicStub monStubRmi;

    public SmartProxy() throws RemoteException{
        monNom = "NoName";
        maDescription = "Inconnu";
        monStubRmi =  new PublicStubImpl();
    }
    
    public SmartProxy(String name, String description, int port) throws RemoteException{
        monNom = name;
        maDescription = "Je suis " + name + " et voici ma description : " + description + " (dans proxy)";
        monStubRmi =  new PublicStubImpl(name, description, port);
    }
    
    public PublicStubImpl getPublicStubImpl(){
    	return (PublicStubImpl)monStubRmi;
    }
    
	@Override
	public boolean inviter(PublicStub stub) throws RemoteException {
		return monStubRmi.inviter(stub);
	}

	@Override
	public String getDescription() throws RemoteException {
		return maDescription;
	}
	
	@Override
	public String getName() throws RemoteException {
		return this.monNom;
	}

	@Override
	public Wall accept(PublicStub stub, Wall wall) throws RemoteException {
		return monStubRmi.accept(stub, wall);
	}

	public List<Wall> getMursAmis() {
		return this.getPublicStubImpl().getMursAmis();
	}

	public List<PublicStub> getRequetesEnAttente() {
		return this.getPublicStubImpl().getRequetesEnAttente();
	}
	
}