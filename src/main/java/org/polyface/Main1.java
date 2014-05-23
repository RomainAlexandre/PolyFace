package org.polyface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main1 {
	public static void main(String[] args) throws RemoteException, NotBoundException {
		User romain = new User("Romain", "Etudiant", 10003);
		
		User cecile = new User("Cecile", "Etudiante", 10007);
		PublicStub romainStub = cecile.findPublicStub("Romain");
		
		System.out.println("Cecile : J'invite " + romainStub.getDescription());
		romainStub.inviter(cecile.getMonPublicStub());
		
		System.out.println("Romain : J'accepte");
		romain.accepter(0);
		
		cecile.afficherAmis();
		
		romain.ecrireSurMonMur("C'est bientot les partiels ~");
		romain.ecrireSurMonMur("Je spam mon mur");

		cecile.visiterMur(0);
		
	}
}
