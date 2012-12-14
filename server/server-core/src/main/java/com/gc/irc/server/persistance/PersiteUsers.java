package com.gc.irc.server.persistance;

import java.util.ArrayList;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.utils.IOUtils;

/**
 * Persit the list of Connected users.
 * 
 * @author gcauchis
 * 
 */
public class PersiteUsers extends Thread {

    /** The list users. */
    private ArrayList<IRCUser> listUsers;

    /**
     * Start a Thread to persist.
     * 
     * @param listUsers
     *            List to persist.
     */
    public static void persistListUser(final ArrayList<IRCUser> listUsers) {
        final PersiteUsers persisteUser = new PersiteUsers(listUsers);
        persisteUser.start();
    }

    /**
     * Instantiates a new persite users.
     * 
     * @param listUsers
     *            the list users
     */
    public PersiteUsers(final ArrayList<IRCUser> listUsers) {
        this.listUsers = listUsers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        if (listUsers != null && listUsers.size() > 0) {
            IOUtils.writeFile("listUser.xml", generateXML());
        }
    }

    /**
     * Generat xml code.
     * 
     * @return xml code in a String.
     */
    private String generateXML() {
        String result = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\" ?>\n\n";
        result += "<listeUsers>\n";
        for (final IRCUser user : listUsers) {
            result += user.toStringXML("\t");
        }

        result += "</listeUsers>\n";

        return result;
    }

}