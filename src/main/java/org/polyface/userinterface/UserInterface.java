package org.polyface.userinterface;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.polyface.mur.Message;
import org.polyface.mur.Wall;
import org.polyface.user.PublicStub;
import org.polyface.user.User;

public class UserInterface {

	private User user;
	private Registry reg;
	private List<String> commandesPrincipales;

	public UserInterface(User u) throws RemoteException {
		this.user = u;
		try {
			reg = LocateRegistry.createRegistry(2001);
		} catch (Exception ee) {
			reg = LocateRegistry.getRegistry(2001);
		}
		System.out
				.println("*** Enregistrement dans le registre de l'utilisateur ***");
		reg.rebind("rmi://localhost:2001/Facebook" + user.getName(),
				(PublicStub) user.getMonPublicStub());
		afficherEtape("Bienvenue " + user.getName() + "!");
		this.initCommandes();
	}

	/**
	 * initialise les commandes disponibles pour l'utilisateur
	 */
	private void initCommandes() {
		commandesPrincipales = new LinkedList<String>();
		commandesPrincipales.add("Afficher mon mur.");
		commandesPrincipales.add("Afficher mes amis.");
		commandesPrincipales.add("Voir mes demandes d'amis.");
		commandesPrincipales.add("Afficher tous les utilisateurs.");
		commandesPrincipales.add("Faire une demande d'amis.");
		commandesPrincipales.add("Poster un message.");
		commandesPrincipales.add("Quitter.");
	}

	private PublicStub getPublicStub(String name) throws AccessException,
			RemoteException, NotBoundException {
		return (PublicStub) reg.lookup("rmi://localhost:2001/Facebook" + name);
	}

	public void start() {
		this.menuPrincipal();
	}

	/**
	 * Affiche le menu principal et gere les actions
	 * commandesPrincipales.add("1 Afficher mon mur.");
	 * commandesPrincipales.add("2 Afficher mes amis.");
	 * commandesPrincipales.add("3 Voir mes demandes d'amis.");
	 * commandesPrincipales.add("4 Afficher tous les utilisateurs.");
	 * commandesPrincipales.add("5 Afficher les amis d'amis.");
	 * commandesPrincipales.add("6 Faire une demande d'amis.");
	 * commandesPrincipales.add("7 Poster un message.");
	 * commandesPrincipales.add("8 Quitter.");
	 */
	private void menuPrincipal() {
		int n = 0;
		while (n != 7) {
			System.out.println("\nQue souhaitez-vous faire? ");
			n = this.displayMenu(this.commandesPrincipales, false);
			switch (n) {
			case 1:
				this.displayMyWall();
				break;
			case 2:
				this.displayMyFriends();
				break;
			case 3:
				this.displayMyDemands();
				break;
			case 4:
				this.displayUsers();
				break;
			case 5:
				this.inviteFriend();
				break;
			case 6:
				this.postMessage();
				break;
			default:
				this.afficherEtape("A bientot!");
				break;
			}
		}
	}

	private void displayUsers() {
		try {
			String[] list = reg.list();
			for(String s : list){
				System.out.println(s.replaceFirst("rmi://localhost:2001/Facebook", ""));
			}
		} catch (Exception e) {
			System.out.println("Erreur lors de la recuperation de la liste");
		}
		
	}

	private void inviteFriend() {
		System.out.println("Rentrez le nom de l'utilisateur à ajouter :");
		Scanner sc = new Scanner(System.in);
		String name = sc.next();
		PublicStub stub;
		try {
			stub = this.getPublicStub(name);
		} catch (Exception e) {
			System.out.println("Utilisateur '" + name + "' non trouvé!");
			return;
		}
		try {
			if (stub.inviter(user.getMonPublicStub()))
				System.out.println("Demande envoyée!");
			else
				System.out.println("Impossible d'ajouter cette personne");
		} catch (RemoteException e) {
			System.out.println("Erreur lors de l'envoi de la demande.");
		}
	}

	private void displayMyDemands() {
		List<PublicStub> demandes;
		demandes = user.getMonPublicStub().getRequetesEnAttente();
		if (demandes.isEmpty()) {
			System.out.println("Pas de demandes!");
		} else {
			this.acceptDemands(demandes);
		}
	}

	private void acceptDemands(List<PublicStub> demandes) {
		while (!demandes.isEmpty()) {
			System.out.println("*** Mes demandes d'ami : ***");
			this.displayPublicStubMenu(demandes);
			System.out.println("Entrer le numéro de la personne à accepter");
			int n = getEntry(demandes.size() + 1);
			if (n <= demandes.size()) {
				try {
					user.accepter(n - 1);
					System.out.println("*** Demande acceptée! ***");
				} catch (RemoteException e) {
					System.out.println("Erreur lors de l'acceptation");
				}
			} else
				return;
			demandes = user.getMonPublicStub().getRequetesEnAttente();
		}
	}

	/**
	 * Poste un message sur son mur
	 */
	private void postMessage() {
		System.out.println("Entrer le message : ");
		Scanner sc = new Scanner(System.in);
		String message = sc.next();
		try {
			this.user.ecrireSurMonMur(message);
			System.out.println("*** Message posté! ***");
		} catch (RemoteException e) {
			System.out.println("Erreur lors de l'ecriture du message");
		}
	}

	/**
	 * Affiche le mur de l'utilisateur
	 * 
	 * @throws RemoteException
	 */
	private void displayMyWall() {
		List<Message> messages;
		try {
			messages = user.getMessagesMur();
			if (messages.isEmpty()) {
				System.out.println("Pas de message à afficher!");
			} else {
				System.out.println("*** Mon mur : ***");
				for (Message m : messages)
					System.out.println(m);
			}
		} catch (RemoteException e) {
			System.out.println("Erreur lors de la recuperation du mur");
		}
	}

	/**
	 * Affiche le mur du n ieme ami de l'utilisateur
	 * 
	 * @throws RemoteException
	 */
	private void displayWall(int n) throws RemoteException {
		Wall w = user.getMonPublicStub().getMursAmis().get(n);
		List<Message> m = w.visiterMur();
		if (m.isEmpty()) {
			System.out.println("*** Pas de messages ***");
		} else {
			for (Message s : w.visiterMur()) {
				System.out.println(s);
			}
		}
	}

	private void displayPublicStubMenu(List<PublicStub> amis) {
		try {
			for (int i = 1; i <= amis.size(); i++) {
				System.out
						.println(i + " - " + amis.get(i - 1).getDescription());
			}
		} catch (RemoteException e) {
			return;
		}
		System.out.println(amis.size() + 1 + " - Retour.");
	}

	/**
	 * Affiche les amis de l'utilisateur
	 * 
	 * @throws RemoteException
	 */
	private void displayMyFriends() {
		List<PublicStub> amis;
		amis = user.getMonMur().getListeAmis();
		if (amis.isEmpty()) {
			System.out.println("Pas d'amis!");
		} else {
			System.out.println("*** Mes amis : ***");
			System.out.println("Afficher le mur de quel ami?");
			this.displayPublicStubMenu(amis);
			int n = getEntry(amis.size() + 1);
			try {
				if (n <= amis.size())
					this.displayWall(n - 1);
			} catch (RemoteException e) {
				System.out.println("Erreur lors du chargement du mur.");
			}
		}
	}

	/**
	 * Affiche les commandes et renvoie la commande entree par l'utilisateur
	 * 
	 * @return
	 */
	private int displayMenu(List<?> commandes, boolean retour) {
		for (int i = 1; i <= commandes.size(); i++) {
			System.out.println(i + " - " + commandes.get(i - 1).toString());
		}
		if (retour) {
			System.out.println(commandes.size() + 1 + " - Retour.");
		}
		int s = retour ? commandes.size() : commandes.size() + 1;
		return getEntry(s);
	}

	private int getEntry(int s) {
		// init des variables de saisie
		String saisie = null;
		Scanner sc = new Scanner(System.in);
		// boucler tant que la saisie n'est pas valide
		do {
			saisie = sc.next();
		} while (!checkMenu(s, saisie));
		// renvoie le choix selectionne
		return Integer.parseInt(saisie);
	}

	private void afficherEtape(String s) {
		System.out.println();
		System.out.println("-----------------------------------");
		System.out.println(s);
		System.out.println("-----------------------------------");
	}

	/**
	 * Methode qui verifie si la saisie de choix dans un menu est correct
	 */
	private boolean checkMenu(int n, String saisie) {
		try {
			Integer.parseInt(saisie);
		} catch (NumberFormatException e) {
			return false;
		}
		int choix = Integer.parseInt(saisie);
		if (Integer.toString(choix).length() > Integer.toString(n).length())
			return false;
		if (choix < 1 || choix > n)
			return false;
		return true;
	}
}
