package com.gc.irc.server.thread.api;

import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.Message;

public interface IGestionClientBean extends Runnable {

    /**
     * Finalize Thread.
     * 
     * Close all Connection.
     */
    void disconnectUser();

    /**
     * Get the id of the Thread. <strong>Warning : </strong> This id is not the
     * user id.
     * 
     * @return Id of this.
     */
    int getIdThread();

    /**
     * Get the user connected to this Thread.
     * 
     * @return User connected to this Thread.
     */
    User getUser();

    /**
     * Send message objet in socket.
     * 
     * @param message
     *            the message
     */
    void sendMessageObjetInSocket(final Message message);

}