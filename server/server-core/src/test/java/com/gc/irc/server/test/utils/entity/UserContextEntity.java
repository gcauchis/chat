package com.gc.irc.server.test.utils.entity;

import com.gc.irc.common.connector.ConnectionHandler;
import com.gc.irc.common.entity.User;
import com.gc.irc.server.test.handler.IMessageHandlerTester;

/**
 * The Class UserContextEntity.
 *
 * @author gcauchis
 * @version 0.0.4
 * @since 0.0.4
 */
public class UserContextEntity {

    /** The connection user. */
    private ConnectionHandler connectionUser;

    /** The message handler user. */
    private IMessageHandlerTester messageHandler;

    /** The user. */
    private User user;

    /**
     * Instantiates a new user context entity.
     *
     * @param user
     *            the user
     * @param connectionUser
     *            the connection user
     */
    public UserContextEntity(final User user, final ConnectionHandler connectionUser) {
        super();
        this.user = user;
        this.connectionUser = connectionUser;
    }

    /**
     * Instantiates a new user context entity.
     *
     * @param user
     *            the user
     * @param connectionUser
     *            the connection user
     * @param messageHandler
     *            the message handler
     */
    public UserContextEntity(final User user, final ConnectionHandler connectionUser, final IMessageHandlerTester messageHandler) {
        super();
        this.user = user;
        this.connectionUser = connectionUser;
        setMessageHandler(messageHandler);
    }

    /**
     * Clean message handler.
     */
    public void cleanMessageHandler() {
        setMessageHandler(null);
    }

    /**
     * Disconnect.
     */
    public void disconnect() {
        connectionUser.disconnect();
    }

    /**
     * Gets the connection user.
     *
     * @return the connectionUser
     */
    public ConnectionHandler getConnectionUser() {
        return connectionUser;
    }

    /**
     * <p>Getter for the field <code>messageHandler</code>.</p>
     *
     * @return the messageHandler
     */
    public IMessageHandlerTester getMessageHandler() {
        return messageHandler;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the connection user.
     *
     * @param connectionUser
     *            the connectionUser to set
     */
    public void setConnectionUser(final ConnectionHandler connectionUser) {
        this.connectionUser = connectionUser;
    }

    /**
     * <p>Setter for the field <code>messageHandler</code>.</p>
     *
     * @param messageHandler
     *            the messageHandler to set
     */
    public final void setMessageHandler(final IMessageHandlerTester messageHandler) {
        this.messageHandler = messageHandler;
        connectionUser.setMessageHandler(messageHandler);
    }

    /**
     * Sets the user.
     *
     * @param user
     *            the user to set
     */
    public void setUser(final User user) {
        this.user = user;
    }

}
