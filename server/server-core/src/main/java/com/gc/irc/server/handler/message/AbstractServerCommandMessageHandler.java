package com.gc.irc.server.handler.message;

import org.springframework.beans.factory.annotation.Autowired;

import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.service.IAuthenticationService;
import com.gc.irc.server.service.IUserPictureService;

/**
 * The Class AbstractServerCommandMessageHandler.
 *
 * @param <MSG>
 *            the generic type
 * @author gcauchis
 * @version 0.0.4
 */
public abstract class AbstractServerCommandMessageHandler<MSG extends Message> extends AbstractServerMessageHandler<MSG> {

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
