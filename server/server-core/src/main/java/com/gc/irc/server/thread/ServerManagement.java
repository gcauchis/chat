package com.gc.irc.server.thread;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractRunnable;
import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.bridge.ServerBridgeConsumer;
import com.gc.irc.server.bridge.ServerBridgeConsumerFactory;
import com.gc.irc.server.bridge.ServerBridgeException;
import com.gc.irc.server.client.connector.ClientConnection;
import com.gc.irc.server.client.connector.management.UsersConnectionsManagement;
import com.gc.irc.server.core.user.management.UserManagement;
import com.gc.irc.server.core.user.management.UserManagementAware;
import com.gc.irc.server.handler.message.ServerMessageHandler;

/**
 * Thread manager.
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Component("serverManagement")
public class ServerManagement extends AbstractRunnable implements ServerManager,UserManagementAware {

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
    private ServerBridgeConsumer serverBridgeConsumer;

    /** The server bridge consumer factory. */
    private ServerBridgeConsumerFactory serverBridgeConsumerFactory;

    /** The server message handlers. */
    private List<ServerMessageHandler> serverMessageHandlers;

    /** The parent. */
    private UsersConnectionsManagement usersConnectionsManagement;
    
    /** The user management */
    private UserManagement userManagement;

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
    /** {@inheritDoc} */
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
    /** {@inheritDoc} */
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
    /** {@inheritDoc} */
    @Override
    public int getNbUser() {
        /**
         * Get the number of connected client (for JMX)
         */
        final List<ClientConnection> clientConnecter = usersConnectionsManagement.getClientConnected();
        return clientConnecter.size();
    }

    /**
     * {@inheritDoc}
     *
     * Return the list of online users (for JMX).
     */
    @Override
    public String getUserList() {
        String result = "";
        for (final User u : userManagement.getAllUsers()) {
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
    private void handleMessage(final Message message) {

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

        for (final ServerMessageHandler serverMessageHandler : serverMessageHandlers) {
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
    /** {@inheritDoc} */
    @Override
    public String kickUser(final int userID) {
        /**
         * Kick the user with the ID userID
         */
        final ClientConnection thClient = usersConnectionsManagement.getGestionClientBeanOfUser(userID);
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
    /** {@inheritDoc} */
    @Override
    public void run() {
        getLog().debug(id + " Start");
        init();

        while (true) {
            waitAndHandleJMSMessage();
        }
    }

    /**
     * <p>Setter for the field <code>serverBridgeConsumerFactory</code>.</p>
     *
     * @param serverBridgeConsumerFactory
     *            the serverBridgeConsumerFactory to set
     */
    @Autowired
    public void setServerBridgeConsumerFactory(final ServerBridgeConsumerFactory serverBridgeConsumerFactory) {
        this.serverBridgeConsumerFactory = serverBridgeConsumerFactory;
    }

    /**
     * Sets the server message handlers.
     *
     * @param serverMessageHandlers
     *            the new server message handlers
     */
    @Autowired
    public void setServerMessageHandlers(final List<ServerMessageHandler> serverMessageHandlers) {
        this.serverMessageHandlers = serverMessageHandlers;
    }

    /**
     * Sets the users connections management.
     *
     * @param usersConnectionsManagement
     *            the new users connections management
     */
    @Autowired
    public void setUsersConnectionsManagement(final UsersConnectionsManagement usersConnectionsManagement) {
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
    
    /** {@inheritDoc} */
    @Override
	@Autowired
	public void setUserManagement(UserManagement userManagement) {
		this.userManagement = userManagement;
	}

}
