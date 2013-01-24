package com.gc.irc.server.thread.impl;

import java.util.List;

import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;

import com.gc.irc.common.abs.AbstractRunnable;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.handler.message.api.IServerMessageHandler;
import com.gc.irc.server.jms.utils.JMSConnectionUtils;
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

    /** The message consumer. */
    private MessageConsumer messageConsumer;

    /** The server message handlers. */
    private List<IServerMessageHandler> serverMessageHandlers;

    /** The parent. */
    private IUsersConnectionsManagement usersConnectionsManagement;

    /**
     * Builds the JMS message consumer.
     */
    private void buildMessageConsumer() {
        try {
            getLog().debug(id + " Create JMS Consumer");
            messageConsumer = JMSConnectionUtils.createConsumer();
        } catch (final JMSException e) {
            getLog().error(id + " Fail to create JMS Consumer : ", e);
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
            messageConsumer.close();
        } catch (final JMSException e) {
            getLog().warn(id + " Problem when close the messageConsumer : " + e.getMessage());
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
    private void handleMessage(final ObjectMessage message) {
        getLog().debug(id + " Handle received Message.");
        if (message == null) {
            getLog().debug("Null message to handle");
            return;
        }

        /**
         * Update of the number of messages.
         */
        synchronized (nbMessage) {
            nbMessage++;
        }

        /**
         * Extract Message
         */
        IRCMessage messageObj = null;
        try {
            getLog().debug(id + " Extract Message receive in JMS");
            messageObj = (IRCMessage) message.getObject();
        } catch (final JMSException e) {
            getLog().warn(id + " Fail to extract Message receive in JMS : " + e.getMessage());
        }
        if (messageObj == null) {
            getLog().debug("Null messageObj to handle");
            return;
        }

        getLog().debug("Handle {}", messageObj);

        for (final IServerMessageHandler serverMessageHandler : serverMessageHandlers) {
            if (serverMessageHandler.isHandled(messageObj)) {
                serverMessageHandler.handle(messageObj);
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
            getLog().debug(id + " Wait for a message in JMS Queue");
            handleMessage((ObjectMessage) messageConsumer.receive());
        } catch (final IllegalStateException e) {
            getLog().error("JMS in bad State", e);
            System.exit(-1);
        } catch (final JMSException e) {
            getLog().warn(id + " Fail to receive message in JMS Queue : ", e);
            try {
                messageConsumer.close();
            } catch (final JMSException e1) {
                getLog().warn(id + " Fail to close messageConsumer Queue : ", e1);
            }
            buildMessageConsumer();
        }
    }
}
