package com.gc.irc.server.client.connector.objectstream;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.gc.irc.common.protocol.Message;
import com.gc.irc.common.utils.IOStreamUtils;
import com.gc.irc.server.client.connector.AbstractClientConnection;
import com.gc.irc.server.client.connector.ClientConnection;
import com.gc.irc.server.core.user.management.UserManagementAware;

/**
 * Communication between the Client and the server using an {@link java.io.ObjectOutputStream}.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public class ObjectStreamClientConnection extends AbstractClientConnection implements ClientConnection, UserManagementAware {


    /** The client socket. */
    private final Socket clientSocket;

    /** The in object. */
    private ObjectInputStream inObject;

    /** The out object. */
    private ObjectOutputStream outObject;

    /**
     * Builder who initialize the TCP connection.
     *
     * @param clientSocket
     *            Client's Socket.
     */
    public ObjectStreamClientConnection(final Socket clientSocket) {
        getLog().info(getId() + " Initialisation du thread.");
        this.clientSocket = clientSocket;

        try {
            getLog().debug(getId() + " Create inObject");
            inObject = new ObjectInputStream(clientSocket.getInputStream());
            getLog().debug(getId() + " Create outObject");
            outObject = new ObjectOutputStream(clientSocket.getOutputStream());

        } catch (final IOException e) {
            getLog().warn(getId() + " Fail to open Client's Steams to " + clientSocket.getInetAddress() + " : ", e);
        }
        getLog().debug(getId() + " end init");
    }

    /** {@inheritDoc} */
    @Override
    protected void disconnect() {
    	 /**
         * Closing Socket.
         */
        try {
            getLog().info(getId() + " Closing Client's connection " + clientSocket.getInetAddress() + ".");
            if (!clientSocket.isInputShutdown()) {
                getLog().debug(getId() + " Closing inObject");
                inObject.close();
            }
            if (!clientSocket.isOutputShutdown()) {
                getLog().debug(getId() + " Closing outObject");
                outObject.close();
            }
            if (!clientSocket.isClosed()) {
                getLog().debug(getId() + " Closing clientSocket");
                clientSocket.close();
            }
        } catch (final IOException e) {
            getLog().warn(getId() + " Fail to close Client's connection " + clientSocket.getInetAddress() + " : " + e.getMessage());
        }
    	
    }

    /**
     * Wait and Receive a message send by the client.
     *
     * @return Message received.
     */
    public Message receiveMessage() {
        Message message = null;
        try {
            getLog().debug(getId() + " Wait for a message in the socket.");
            message = IOStreamUtils.receiveMessage(inObject);
            checkMessage(message);
        } catch (final EOFException e) {
            getLog().debug(getId() + " Stream seem to be closed by client.");
            socketAlive();
        } catch (final ClassNotFoundException | IOException e) {
            getLog().info(getId() + " Fail to receive a message : ", e);
            socketAlive();
        }
        return message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.thread.impl.IGestionClientBean#envoyerMessageObjetSocket
     * (com.gc.irc.common.protocol.IRCMessage)
     */
    /** {@inheritDoc} */
    @Override
    public void send(final Message message) {
        try {
            /**
             * Synchronize the socket.
             */
            if (!clientSocket.isOutputShutdown()) {
                synchronized (inObject) {
                    synchronized (outObject) {
                        getLog().debug(getId() + " Send message to " + (getUser() == null ? "Unkwown" :getUser().getNickName()));
                        if (clientSocket.isConnected()) {
                            IOStreamUtils.sendMessage(outObject, message);
                        } else {
                            getLog().warn(getId() + " Socket not connected !");
                        }
                    }
                }
            } else {
                getLog().warn(getId() + " Fail to send message. Finalize because output is shutdown.");
                disconnectUser();
            }
        } catch (final IOException e) {
            getLog().warn(getId() + " Fail to send the message : " + e.getMessage());
            socketAlive();
        }
    }

    /**
     * Test if the socket is already open. If socket is closed or a problem is
     * remark the thread is finalize.
     */
    private void socketAlive() {
        getLog().debug(getId() + " Test if the socket have no problem.");
        if (clientSocket.isClosed() || clientSocket.isInputShutdown() || clientSocket.isOutputShutdown() || !clientSocket.isBound()
                || !clientSocket.isConnected()) {
            getLog().error(getId() + " A problem find on the Socket. Closing Connection");
            disconnectUser();
        }
    }

}
