package com.gc.irc.server.persistance;

import java.util.ArrayList;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.utils.IOUtils;

/**
 * Persit the list of Connected users.
 * @author gcauchis
 *
 */
public class PersiteUsers extends Thread {
	private ArrayList<IRCUser> listUsers;
	
	/**
	 * Start a Thread to persist.
	 * @param listUsers List to persist.
	 */
	public static void persistListUser(ArrayList<IRCUser> listUsers) {
		PersiteUsers persisteUser = new PersiteUsers(listUsers);
		persisteUser.start();
	}
	
	public PersiteUsers(ArrayList<IRCUser> listUsers) {
		this.listUsers = listUsers;
	}
	
	@Override
	public void run() {
		if (listUsers != null && listUsers.size() > 0) {
			IOUtils.ecritFichier("listUser.xml", generateXML());
		}
	}
	
	/**
	 * Generat xml code.
	 * @return xml code in a String.
	 */
	private String generateXML() {
		String result = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\" ?>\n\n";
		result += "<listeUsers>\n";
		for (IRCUser user : listUsers) {
			result += user.toStringXML("\t");
		}
		
		result += "</listeUsers>\n";
		
		return result;
	}
	
	
}
