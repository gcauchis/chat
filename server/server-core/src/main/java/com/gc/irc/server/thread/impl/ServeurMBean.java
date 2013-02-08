package com.gc.irc.server.thread.impl;

import java.util.List;

import com.gc.irc.common.abs.AbstractRunnable;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.server.bridge.api.IServerBridgeConsumer;
import com.gc.irc.server.bridge.api.IServerBridgeConsumerFactory;
import com.gc.irc.server.bridge.api.ServerBridgeException;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.handler.message.api.IServerMessageHandler;
import com.gc.irc.server.thread.api.IGestionClientBean;
import com.gc.irc.server.thread.api.IServeurMBean;

/**
 * Thread manager.
 * 
 * @author gcauchis
 * 
 */
public class ServeurMBean extends AbstractRunnable implements IServeurMBean {

    /** The nb message. */
    private static Long nbMessage = 0L;

    /** The nb thread. */
    private static int nbThread = 0;

    /**
     * Gets the nb thread.
     * 
     * @return the nb thread
     */
    private static synchronized int getNbThread() {
        nbThread++;
        return nbThread;
    }

    /** The id. */
    private final int id = getNbThread();

    /** The server bridge consumer. */
    private IServerBridgeConsumer serverBridgeConsumer;

    /** The server bridge consumer factory. */
    private IServerBridgeConsumerFactory serverBridgeConsumerFactory;

    /** The server message handlers. */
    private List<IServerMessageHandler> serverMessageHandlers;

    /** The parent. */
    private IUsersConnectionsManagement usersConnectionsManagement;

    /**
     * Builds the message consumer.
     */
    private void buildMessageConsumer() {

        try {
            getLog().debug("Build Message consumer");
            serverBridgeConsumer = serverBridgeConsumerFactory.getInstance();
        } catch (final ServerBridgeException e) {
            getLog().error("Fail to create server bridge consumer", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.api.IServeurMBean#close()
     */
    @Override
    public void close() {
        getLog().debug("Finalize the Thread");
        try {
            serverBridgeConsumer.close();
        } catch (final ServerBridgeException e) {
            getLog().warn(id + " Problem when close the messageConsumer : ", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.api.IServeurMBean#getNbMessages()
     */
    @Override
    public long getNbMessages() {
        /**
         * Get the number of messages (for JMX)
         */
        return nbMessage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.api.IServeurMBean#getNbUser()
     */
    @Override
    public int getNbUser() {
        /**
         * Get the number of connected client (for JMX)
         */
        final List<IGestionClientBean> clientConnecter = usersConnectionsManagement.getClientConnected();
        return clientConnecter.size();
    }

    /**
     * Return the list of online users (for JMX).
     * 
     * @return the user list
     */
    @Override
    public String getUserList() {
        String result = "";

        for (final IRCUser u : usersConnectionsManagement.getAllUsers()) {
            result += u.getId() + " : " + u.getNickName() + " | ";
        }

        return result;
    }

    /**
     * Handle Message.
     * 
     * @param message
     *            Message received.
     */
    private void handleMessage(final IRCMessage message) {

        /**
         * Update of the number of messages.
         */
        synchronized (nbMessage) {
            nbMessage++;
        }

        getLog().debug(id + " Handle received Message.");
        if (message == null) {
            getLog().debug("Null message to handle");
            return;
        }

        getLog().debug("Handle {}", message);

        for (final IServerMessageHandler serverMessageHandler : serverMessageHandlers) {
            if (serverMessageHandler.isHandled(message)) {
                serverMessageHandler.handle(message);
            }
        }

    }

    /**
     * Initialize Thread.
     * 
     * Listening JMS Queue
     */
    private void init() {

        buildMessageConsumer();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.api.IServeurMBean#kickUser(int)
     */
    @Override
    public String kickUser(final int userID) {
        /**
         * Kick the user with the ID userID
         */
        final IGestionClientBean thClient = usersConnectionsManagement.getGestionClientBeanOfUser(userID);
        if (thClient != null) {
            thClient.disconnectUser();
            return "Client successfully kicked";
        } else {
            return "Could not kick client";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        getLog().debug(id + " Start");
        init();

        while (true) {
            waitAndHandleJMSMessage();
        }
    }

    /**
     * @param serverBridgeConsumerFactory
     *            the serverBridgeConsumerFactory to set
     */
    public void setServerBridgeConsumerFactory(final IServerBridgeConsumerFactory serverBridgeConsumerFactory) {
        this.serverBridgeConsumerFactory = serverBridgeConsumerFactory;
    }

    /**
     * Sets the server message handlers.
     * 
     * @param serverMessageHandlers
     *            the new server message handlers
     */
    public void setServerMessageHandlers(final List<IServerMessageHandler> serverMessageHandlers) {
        this.serverMessageHandlers = serverMessageHandlers;
    }

    /**
     * Sets the users connections management.
     * 
     * @param usersConnectionsManagement
     *            the new users connections management
     */
    public void setUsersConnectionsManagement(final IUsersConnectionsManagement usersConnectionsManagement) {
        this.usersConnectionsManagement = usersConnectionsManagement;
    }

    /**
     * Wait and handle jms message.
     */
    private void waitAndHandleJMSMessage() {
        try {
            getLog().debug(id + " Wait for a message in message consumer");
            handleMessage(serverBridgeConsumer.receive());
        } catch (final ServerBridgeException e) {
            getLog().warn(id + " Fail to receive message", e);
            try {
                serverBridgeConsumer.close();
            } catch (final ServerBridgeException e1) {
                getLog().warn(id + " Fail to close messageConsumer : ", e1);
            }
            buildMessageConsumer();
        }
    }

}
