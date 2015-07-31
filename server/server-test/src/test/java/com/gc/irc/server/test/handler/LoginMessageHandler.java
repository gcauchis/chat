package com.gc.irc.server.test.handler;

import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.common.protocol.notice.MessageNoticeContactsList;
import com.gc.irc.common.protocol.notice.MessageNoticeLogin;
import com.gc.irc.common.protocol.notice.MessageNoticeRegister;

/**
 * The Class LoginMessageHandler.
 *
 * @author gcauchis
 * @version 0.0.4
 * @since 0.0.4
 */
public class LoginMessageHandler extends AbstractMessageHandlerTester {

    /** The contact list received. */
    boolean contactListReceived = false;

    /** The login. */
    private User login;

    /** The login validated. */
    boolean loginValidated = false;

    /**
     * Gets the login.
     *
     * @return the login
     */
    public User getLogin() {
        return login;
    }

    /**
     * {@inheritDoc}
     *
     * Handle internal.
     */
    @Override
    protected void handleInternal(final Message message) {
        if (message instanceof MessageNoticeLogin) {
            login = ((MessageNoticeLogin) message).getUser();
            if (login != null) {
                loginValidated = true;
                getLog().info("USER : " + login.toStringXML(""));
            }
        } else if (message instanceof MessageNoticeRegister) {
            login = ((MessageNoticeRegister) message).getUser();
            if (login != null) {
                loginValidated = true;
                getLog().info("REGISTER USER : " + login.toStringXML(""));
            }
        } else if (message instanceof MessageNoticeContactsList) {
            contactListReceived = true;
            getLog().info("Contact list received : " + message);
        }
    }

    /**
     * Checks if is login validated.
     *
     * @return a boolean.
     */
    public boolean isLoginValidated() {
        return loginValidated && contactListReceived;
    }

    /**
     * {@inheritDoc}
     *
     * Reset insernal.
     */
    @Override
    protected void resetInsernal() {
        loginValidated = false;
    }

}
