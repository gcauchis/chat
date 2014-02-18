package com.gc.irc.server.handler.message.abs;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.core.user.management.api.IUserManagement;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.core.user.management.api.UserManagementAware;
import com.gc.irc.server.handler.message.api.IServerMessageHandler;

/**
 * The Class AbstractServerMessageHandler.
 */
public abstract class AbstractServerMessageHandler<MSG extends Message> extends AbstractLoggable implements IServerMessageHandler, UserManagementAware {

    /** The msg class. */
    private Class<MSG> msgClass;

    /** The users connections management. */
    @Autowired
    private IUsersConnectionsManagement usersConnectionsManagement;
    
    private IUserManagement userManagement;

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
     * @param usersConnectionsManagement
     *            the usersConnectionsManagement to set
     */
    public final void setUsersConnectionsManagement(final IUsersConnectionsManagement usersConnectionsManagement) {
        this.usersConnectionsManagement = usersConnectionsManagement;
    }
    
    @Override
	@Autowired
	public void setUserManagement(IUserManagement userManagement) {
		this.userManagement = userManagement;
	}
    
    protected IUserManagement getUserManagement() {
		return userManagement;
	}

}
