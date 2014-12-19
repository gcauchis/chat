package com.gc.irc.server.thread;

/**
 * The Interface IServeurMBean.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface ServerManager extends Runnable {

    /**
     * Close.
     */
    void close();

    /**
     * Gets the nb messages.
     *
     * @return the nb messages
     */
    long getNbMessages();

    /**
     * Gets the nb user.
     *
     * @return the nb user
     */
    int getNbUser();

    /**
     * Gets the user list.
     *
     * @return the user list
     */
    String getUserList();

    /**
     * Kick user.
     *
     * @param userID
     *            the user id
     * @return the string
     */
    String kickUser(int userID);

}
