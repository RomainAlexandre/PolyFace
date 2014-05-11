package org.polyface;

import java.rmi.Remote;

public interface PublicStub extends Remote{

	public void inviter(PublicStub stub);
	public String getDescription();
	public Wall accept(Wall wall);
	
}
