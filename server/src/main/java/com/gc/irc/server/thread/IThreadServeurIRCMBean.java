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
    public int getNbUser();

    /**
     * Gets the nb messages.
     * 
     * @return the nb messages
     */
    public int getNbMessages();

    /**
     * Gets the user list.
     * 
     * @return the user list
     */
    public String getUserList();

    /**
     * Kick user.
     * 
     * @param userID
     *            the user id
     * @return the string
     */
    public String kickUser(int userID);

}
