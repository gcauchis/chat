package com.gc.irc.server.test.handler;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.notice.IRCMessageNoticeLogin;
import com.gc.irc.common.protocol.notice.IRCMessageNoticeRegister;

/**
 * The Class LoginMessageHandler.
 */
public class LoginMessageHandler extends AbstractMessageHandlerTester {

    /** The login. */
    private IRCUser login;

    /** The login validated. */
    boolean loginValidated = false;

    /**
     * Handle internal.
     * 
     * @param message
     *            the message
     */
    @Override
    protected void handleInternal(final IRCMessage message) {
        if (message instanceof IRCMessageNoticeLogin) {
            login = ((IRCMessageNoticeLogin) message).getUser();
            if (login != null) {
                loginValidated = true;
                getLog().info("USER : " + login.toStringXML(""));
            }
        } else if (message instanceof IRCMessageNoticeRegister) {
            login = ((IRCMessageNoticeRegister) message).getUser();
            if (login != null) {
                loginValidated = true;
                getLog().info("REGISTER USER : " + login.toStringXML(""));
            }
        }
    }

    /**
     * Checks if is login validated.
     * 
     * @return true, if is login validated
     */
    public boolean isLoginValidated() {
        return loginValidated;
    }

    /**
     * Reset insernal.
     */
    @Override
    protected void resetInsernal() {
        loginValidated = false;
    }

    /**
     * Gets the login.
     * 
     * @return the login
     */
    public IRCUser getLogin() {
        return login;
    }

}
