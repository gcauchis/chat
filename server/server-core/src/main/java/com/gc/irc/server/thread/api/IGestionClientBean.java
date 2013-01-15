package com.gc.irc.server.thread.api;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.IRCMessage;

public interface IGestionClientBean extends Runnable {

    /**
     * Finalize Thread.
     * 
     * Close all Connection.
     */
    public abstract void disconnectUser();

    /**
     * Send and IRCMessage to the Client.
     * 
     * @param message
     *            Message to send.
     */
    public abstract void envoyerMessageObjetSocket(final IRCMessage message);

    /**
     * Get the id of the Thread. <strong>Warning : </strong> This id is not the user id.
     * 
     * @return Id of this.
     */
    public abstract int getIdThread();

    /**
     * Get the user connected to this Thread.
     * 
     * @return User connected to this Thread.
     */
    public abstract IRCUser getUser();

}