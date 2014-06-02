package org.polyface.mur;

import java.io.Serializable;

public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	private String message;
	private String author;
	
	public Message(String m, String a){
		this.author=a;
		this.message=m;
	}
	
	@Override
	public String toString() {
		return message + "\n\t--- par " + author;
	}
	
}
