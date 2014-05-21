package org.polyface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;

public class Main2 {
	public static void main(String[] args) throws RemoteException, NotBoundException {
		Registry reg;

		try {
			reg = LocateRegistry.createRegistry(2001);
		} catch (ExportException ee) {
			reg = LocateRegistry.getRegistry(2001);
		}

		User cecileStub = new User("Cecile", 10007);

		// Enregistrer
		reg.rebind("rmi://localhost:2001/FacebookCecile", cecileStub);

		System.out.println("Cecile is in da place !");

		PublicStub romainStub = (PublicStub) reg.lookup("rmi://localhost:2001/FacebookRomain");
		
		System.out.println("Cecile invite " + romainStub.getDescription());
		
		romainStub.inviter(cecileStub);
	}
}
