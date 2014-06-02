package org.polyface.main;

import java.rmi.RemoteException;

import org.polyface.user.User;
import org.polyface.userinterface.UserInterface;

public class MainCecile {

	public static void main(String[] args) throws RemoteException{ 
		User cecile = new User("Cecile", "mouit mouit", 12000);
		UserInterface ui = new UserInterface(cecile);
		ui.start();
	}
	
}
