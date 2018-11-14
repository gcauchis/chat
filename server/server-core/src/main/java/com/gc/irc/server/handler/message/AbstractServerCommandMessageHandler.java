package com.gc.irc.server.handler.message;

import org.springframework.beans.factory.annotation.Autowired;

import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.service.AuthenticationService;
import com.gc.irc.server.service.UserPictureService;

/**
 * The Class AbstractServerCommandMessageHandler.
 *
 * @param <MSG>
 *            the generic type
 * @version 0.0.4
 * @author x472511
 */
public abstract class AbstractServerCommandMessageHandler<MSG extends Message> extends AbstractServerMessageHandler<MSG> {

    /** The authentication service. */
    @Autowired
    private AuthenticationService authenticationService;

    /** The user picture service. */
    @Autowired
    private UserPictureService userPictureService;

    /**
     * Gets the authentication service.
     *
     * @return the authentication service
     */
    protected AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    /**
     * Gets the user picture service.
     *
     * @return the user picture service
     */
    protected UserPictureService getUserPictureService() {
        return userPictureService;
    }

    /**
     * Sets the authentication service.
     *
     * @param authenticationService
     *            the new authentication service
     */
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Sets the user picture service.
     *
     * @param userPictureService
     *            the new user picture service
     */
    public void setUserPictureService(UserPictureService userPictureService) {
        this.userPictureService = userPictureService;
    }

}
