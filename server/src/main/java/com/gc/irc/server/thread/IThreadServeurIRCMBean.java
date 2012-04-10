package com.gc.irc.server.thread;

/**
 * The Interface ThreadServeurIRCMBean.
 */
public interface IThreadServeurIRCMBean {

    /**
     * Gets the nb user.
     * 
     * @return the nb user
     */
    int getNbUser();

    /**
     * Gets the nb messages.
     * 
     * @return the nb messages
     */
    int getNbMessages();

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
