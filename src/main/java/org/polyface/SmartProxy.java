package org.polyface;

import java.rmi.RemoteException;

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
        maDescription = description;
        monStubRmi =  new PublicStubImpl(name, description, port);
    }
    
    public PublicStubImpl getPublicStubImpl(){
    	return (PublicStubImpl)monStubRmi;
    }
    
	@Override
	public void inviter(PublicStub stub) throws RemoteException {
		monStubRmi.inviter(stub);
	}

	@Override
	public String getDescription() throws RemoteException {
		return "Je suis " + monNom + " et voici ma description : " + maDescription;
	}

	@Override
	public Wall accept(PublicStub stub, Wall wall) throws RemoteException {
		return monStubRmi.accept(stub, wall);
	}
}
