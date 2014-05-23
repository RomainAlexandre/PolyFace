package org.polyface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main1 {
	public static void main(String[] args) throws RemoteException, NotBoundException {
		
		afficherEtape("Creation des utilisateurs");
		// Creation de romain
		User romain = new User("Romain", "Etudiant en 4eme annee a Polytech specialite AL", 10003);
		
		// Creation de Cecile
		User cecile = new User("Cecile", "Etudiante en 4eme annee a Polytech specialite AL", 10007); 

		// Creation d'Adrien
		User adrien = new User("Adrien", "Etudiant en 4eme annee a Polytech specialite IHM", 10010); 
		

		afficherEtape("Cecile et Adrien invite Romain");
		// Cecile invite Romain
		PublicStub romainStub = cecile.findPublicStub("Romain");
		System.out.println("Cecile : J'invite : '" + romainStub.getDescription() +"'");
		romainStub.inviter(cecile.getMonPublicStub());
		
		// Adrien invite Romain
		PublicStub romainStub2 = adrien.findPublicStub("Romain");
		System.out.println("Adrien : J'invite : '" + romainStub2.getDescription()+"'");
		romainStub2.inviter(adrien.getMonPublicStub());
		
		afficherEtape("Romain Accepte la proposition de Cecile et d'Adrien");
		// Romain Accepte la proposition de Cecile
		System.out.println("Romain : J'accepte la demande de Cecile");
		romain.accepter(0);
		
		// Romain Accepte la proposition de Adrien
		System.out.println("Romain : J'accepte la demande d'Adrien");
		romain.accepter(0);
		
		afficherEtape("Romain affiche ses amis");
		// Romain affiche ses amis
		romain.afficherAmis();
		

		afficherEtape("Romain ecrit sur son mur et Cecile visite le mur de Romain");
		// Romain ecrit sur son mur
		romain.ecrireSurMonMur("C'est bientot les partiels ~");

		// Cecile visite le mur de Romain
		cecile.visiterMur(0);

		afficherEtape("Romain ecrit de nouveau sur son mur et Cecile et Adrien visitent le mur de Romain");
		// Romain ecrit de nouveau sur son mur
		romain.ecrireSurMonMur("Je spam mon mur");
		
		// Cecile visite le mur de Romain
		cecile.visiterMur(0);		
		
		// Adrien visite le mur de Romain
		adrien.visiterMur(0);
		
		afficherEtape("Adrien recupere les amis de Romain et invite Cecile");
		// Adrien recupere les amis de Romain et invite Cecile
		Wall romainWall = adrien.getMonPublicStub().getMursAmis().get(0);
		PublicStub cecileStub = romainWall.recupererListeAmis().get(0);
		System.out.println("Adrien : J'invite : '" + cecileStub.getDescription() +"'");
		cecileStub.inviter(adrien.getMonPublicStub());
		
		afficherEtape("Cecile Accepte la proposition de Adrien");
		// Cecile Accepte la proposition de Adrien
		System.out.println("Cecile : J'accepte la demande de Adrien");
		cecile.accepter(0);
	}
	
	
	public static void afficherEtape(String s) {
		System.out.println();
		System.out.println("-----------------------------------");
		System.out.println(s);
		System.out.println("-----------------------------------");
	}
}
