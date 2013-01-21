package com.gc.irc.server.handler.message.abs;

import org.springframework.beans.factory.annotation.Autowired;

import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.server.service.api.IAuthenticationService;
import com.gc.irc.server.service.api.IUserPictureService;

/**
 * The Class AbstractServerCommandMessageHandler.
 * 
 * @param <MSG>
 *            the generic type
 */
public abstract class AbstractServerCommandMessageHandler<MSG extends IRCMessage> extends AbstractServerMessageHandler<MSG> {

    /** The authentication service. */
    @Autowired
    private IAuthenticationService authenticationService;

    /** The user picture service. */
    @Autowired
    private IUserPictureService userPictureService;

    /**
     * Gets the authentication service.
     * 
     * @return the authentication service
     */
    protected IAuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    /**
     * Gets the user picture service.
     * 
     * @return the user picture service
     */
    protected IUserPictureService getUserPictureService() {
        return userPictureService;
    }

    /**
     * Sets the authentication service.
     * 
     * @param authenticationService
     *            the new authentication service
     */
    public void setAuthenticationService(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Sets the user picture service.
     * 
     * @param userPictureService
     *            the new user picture service
     */
    public void setUserPictureService(IUserPictureService userPictureService) {
        this.userPictureService = userPictureService;
    }

}
