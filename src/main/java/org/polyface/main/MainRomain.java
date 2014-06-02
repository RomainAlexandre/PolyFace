package org.polyface.main;

import java.rmi.RemoteException;

import org.polyface.user.User;
import org.polyface.userinterface.UserInterface;

public class MainRomain {

	public static void main(String[] args) throws RemoteException{ 
		User romain = new User("Romain", "Etudiant en 4eme annee a Polytech specialite AL", 12005);
		UserInterface ui = new UserInterface(romain);
		ui.start();
	}
	
}
