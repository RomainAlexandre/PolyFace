package org.polyface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;

public class Main1 {
	public static void main(String[] args) throws RemoteException {
		Registry reg;
		
		try {
			reg = LocateRegistry.createRegistry(2001);
		} catch (ExportException ee) {
			reg = LocateRegistry.getRegistry(2001);
		}

		User romainStub = new User("Romain", 10003);

		// Enregistrer
		reg.rebind("rmi://localhost:2001/FacebookRomain", romainStub);

		System.out.println("Romain is in da place !");
		
		try {
			while(romainStub.getRequetesEnAttente().isEmpty()){
				romainStub = (User) reg.lookup("rmi://localhost:2001/FacebookRomain");
			}
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("pouit");
	}
}
