package com.gc.irc.server.test.handler;

import com.gc.irc.common.api.IIRCMessageHandler;
import com.gc.irc.common.protocol.IRCMessage;

/**
 * The Class LoginMessageHandler.
 */
public class LoginMessageHandler implements IIRCMessageHandler {

	/** The login validated. */
	boolean loginValidated = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gc.irc.common.api.IIRCMessageHandler#handle(com.gc.irc.common.protocol
	 * .IRCMessage)
	 */
	public void handle(final IRCMessage message) {

	}

	/**
	 * Checks if is login validated.
	 * 
	 * @return true, if is login validated
	 */
	public boolean isLoginValidated() {
		return loginValidated;
	}

}
