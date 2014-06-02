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
	private static final String rmiAddress = "rmi://localhost:2001/Facebook";
	private Scanner scanner;

	public UserInterface(User u) throws RemoteException {
		this.user = u;
		try {
			reg = LocateRegistry.createRegistry(2001);
		} catch (Exception ee) {
			reg = LocateRegistry.getRegistry(2001);
		}
		System.out
				.println("*** Enregistrement dans le registre de l'utilisateur ***");
		reg.rebind(rmiAddress + user.getName(),
				(PublicStub) user.getMonPublicStub());
		afficherEtape("Bienvenue " + user.getName() + "!");
		this.initCommandes();
		scanner = new Scanner(System.in);
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
		int n = 0;
		while (n != 7) {
			n = this.displayMenu(this.commandesPrincipales,
					"Que souhaitez-vous faire? ", false);
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
				this.displayAllUsers();
				break;
			case 5:
				this.inviteFriend();
				break;
			case 6:
				this.postMessage();
				break;
			default:
				this.afficherEtape("A bientot!");
				this.scanner.close();
				break;
			}
		}
	}

	/**
	 * Affiche le nom de tous les utilisateurs enregistrés
	 */
	private void displayAllUsers() {
		try {
			String[] list = reg.list();
			for (String s : list) {
				System.out.println(s.replaceFirst(rmiAddress, ""));
			}
		} catch (Exception e) {
			System.out.println("Erreur lors de la recuperation de la liste");
		}
	}

	/**
	 * Demande le nom d'un utilisateur afin de l'inviter
	 */
	private void inviteFriend() {
		System.out.println("Rentrez le nom de l'utilisateur à ajouter :");
		String name = scanner.next();
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

	/**
	 * Gestion des demandes d'amis
	 */
	private void displayMyDemands() {
		List<PublicStub> demandes;
		demandes = user.getMonPublicStub().getRequetesEnAttente();
		if (demandes.isEmpty()) {
			System.out.println("*** Pas de demandes! ***");
		} else {
			this.acceptDemands(demandes);
		}
	}

	/**
	 * Affiche les demandes d'amis et permet de les accepter
	 * @param demandes
	 */
	private void acceptDemands(List<PublicStub> demandes) {
		while (!demandes.isEmpty()) {
			System.out.println("*** Mes demandes d'ami : ***");
			int n = this.displayPublicStubMenu(demandes,
					"Entrer le numéro de la personne à accepter : ");
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
		String message = scanner.next();
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
			displayWall(messages, user.getName());
		} catch (RemoteException e) {
			System.out.println("Erreur lors de la recuperation du mur");
		}
	}

	/**
	 * Affiche liste de messages d'une personne
	 * 
	 * @param messages
	 */
	private void displayWall(List<Message> messages, String name) {
		if (messages.isEmpty()) {
			System.out.println("*** Pas de messages ***");
		} else {
			System.out.println("*** Mur de " + name + " : ***\n");
			for (Message s : messages) {
				System.out.println(s+"\n");
			}
		}
	}

	private int displayPublicStubMenu(List<PublicStub> amis, String message) {
		try {
			for (int i = 1; i <= amis.size(); i++) {
				System.out
						.println(i + " - " + amis.get(i - 1).getDescription());
			}
		} catch (RemoteException e) {
			return -1;
		}
		System.out.println(amis.size() + 1 + " - Retour.");
		System.out.print(message);
		return getEntry(amis.size() + 1);
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
			System.out.println("*** Pas d'amis! ***");
		} else {
			System.out.println("*** Mes amis : ***");
			int n = this.displayPublicStubMenu(amis,
					"Afficher le mur de quel ami? ");
			try {
				if (n <= amis.size()) {
					Wall w = user.getMonPublicStub().getMursAmis().get(n - 1);
					this.displayWall(w.visiterMur(), amis.get(n - 1).getName());
				}
			} catch (RemoteException e) {
				System.out.println("Erreur lors du chargement du mur.");
			}
		}
	}

	/**
	 * Demande a l'utilisateur de rentrer un entier entre 1 et s compris
	 */
	private int getEntry(int s) {
		// init des variables de saisie
		String saisie = null;
		// boucler tant que la saisie n'est pas valide
		do {
			saisie = scanner.next();
		} while (!checkMenu(s, saisie));
		// renvoie le choix selectionne
		return Integer.parseInt(saisie);
	}

	/**
	 * Affiche les commandes et renvoie le numero entre par l'utilisateur
	 * 
	 * @return
	 */
	private int displayMenu(List<?> commandes, String message, boolean retour) {
		System.out.println();
		for (int i = 1; i <= commandes.size(); i++) {
			System.out.println(i + " - " + commandes.get(i - 1).toString());
		}
		if (retour) {
			System.out.println(commandes.size() + 1 + " - Retour.");
		}
		System.out.print(message);
		int s = retour ? commandes.size() : commandes.size() + 1;
		return getEntry(s);
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

	private void afficherEtape(String s) {
		System.out.println();
		System.out.println("-----------------------------------");
		System.out.println(s);
		System.out.println("-----------------------------------");
	}
}
