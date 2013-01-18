package com.gc.irc.server.handler.message.abs;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.handler.message.api.IServerMessageHandler;

/**
 * The Class AbstractServerMessageHandler.
 */
public abstract class AbstractServerMessageHandler<MSG extends IRCMessage> extends AbstractLoggable implements IServerMessageHandler {

    /** The msg class. */
    private Class<MSG> msgClass;

    /** The users connections management. */
    @Autowired
    private IUsersConnectionsManagement usersConnectionsManagement;

    /**
     * Instantiates a new abstract server message handler.
     */
    @SuppressWarnings("unchecked")
    public AbstractServerMessageHandler() {
        final Type genericSuperClass = this.getClass().getGenericSuperclass();
        if (genericSuperClass instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            msgClass = (Class<MSG>) parameterizedType.getActualTypeArguments()[0];
        }
    }

    /**
     * Gets the sender.
     * 
     * @param message
     *            the message
     * @return the sender
     */
    protected final IRCUser getSender(final IRCMessage message) {
        return getUser(message.getFromId());
    }

    /**
     * Gets the user.
     * 
     * @param id
     *            the id
     * @return the user
     */
    protected final IRCUser getUser(final int id) {
        return usersConnectionsManagement.getUser(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.handler.message.api.IServerMessageHandler#handle(com
     * .gc.irc.common.protocol.IRCMessage)
     */
    @SuppressWarnings("unchecked")
    @Override
    public final void handle(final IRCMessage message) {
        if (isHandled(message)) {
            getLog().debug("Handle {}", message);
            internalHandle((MSG) message);
        }
    }

    /**
     * Internal handle.
     * 
     * @param message
     *            the message
     */
    protected abstract void internalHandle(MSG message);

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.handler.message.api.IServerMessageHandler#isHandled
     * (com.gc.irc.common.protocol.IRCMessage)
     */
    @Override
    public final boolean isHandled(final IRCMessage message) {
        return message.getClass() == msgClass;
    }

    /**
     * Send to.
     * 
     * @param message
     *            the message
     * @param toId
     *            the to id
     */
    protected final void sendTo(final IRCMessage message, final int toId) {
        usersConnectionsManagement.sendTo(message, toId);
    }

    /**
     * Send to all users.
     * 
     * @param message
     *            the message
     */
    protected final void sendToAllUsers(final IRCMessage message) {
        usersConnectionsManagement.sendMessageToAllUsers(message);
    }

    /**
     * @param usersConnectionsManagement
     *            the usersConnectionsManagement to set
     */
    public final void setUsersConnectionsManagement(final IUsersConnectionsManagement usersConnectionsManagement) {
        this.usersConnectionsManagement = usersConnectionsManagement;
    }

}
