package com.gc.irc.server.handler.message;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.core.user.management.UserManagement;
import com.gc.irc.server.core.user.management.UserManagementAware;
import com.gc.irc.server.core.user.management.UsersConnectionsManagement;

/**
 * The Class AbstractServerMessageHandler.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public abstract class AbstractServerMessageHandler<MSG extends Message> extends AbstractLoggable implements ServerMessageHandler, UserManagementAware {

    /** The msg class. */
    private Class<MSG> msgClass;

    /** The users connections management. */
    @Autowired
    private UsersConnectionsManagement usersConnectionsManagement;
    
    private UserManagement userManagement;

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
    protected final User getSender(final Message message) {
        return getUser(message.getFromId());
    }
    
    /**
     * Gets the user.
     *
     * @param id
     *            the id
     * @return the user
     */
    protected final User getUser(final long id) {
        return userManagement.getUser(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.api.IServerMessageHandler#handle(com .gc.irc.common.protocol.IRCMessage)
     */
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public final void handle(final Message message) {
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
     * @see com.gc.irc.server.handler.message.api.IServerMessageHandler#isHandled (com.gc.irc.common.protocol.IRCMessage)
     */
    /** {@inheritDoc} */
    @Override
    public final boolean isHandled(final Message message) {
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
    protected final void sendTo(final Message message, final long toId) {
        usersConnectionsManagement.sendTo(message, toId);
    }

    /**
     * Send to all users.
     *
     * @param message
     *            the message
     */
    protected final void sendToAllUsers(final Message message) {
        usersConnectionsManagement.sendMessageToAllUsers(message);
    }

    /**
     * <p>Setter for the field <code>usersConnectionsManagement</code>.</p>
     *
     * @param usersConnectionsManagement
     *            the usersConnectionsManagement to set
     */
    public final void setUsersConnectionsManagement(final UsersConnectionsManagement usersConnectionsManagement) {
        this.usersConnectionsManagement = usersConnectionsManagement;
    }
    
    /** {@inheritDoc} */
    @Override
	@Autowired
	public void setUserManagement(UserManagement userManagement) {
		this.userManagement = userManagement;
	}
    
    /**
     * <p>Getter for the field <code>userManagement</code>.</p>
     *
     * @return a {@link com.gc.irc.server.core.user.management.UserManagement} object.
     */
    protected UserManagement getUserManagement() {
		return userManagement;
	}

}
