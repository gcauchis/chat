package com.gc.irc.server.test.handler;

import org.apache.log4j.Logger;

import com.gc.irc.common.api.IIRCMessageHandler;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.notice.IRCMessageNoticeLogin;
import com.gc.irc.common.protocol.notice.IRCMessageNoticeRegister;

/**
 * The Class LoginMessageHandler.
 */
public class LoginMessageHandler implements IIRCMessageHandler {

	/** The login. */
	private IRCUser login;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger
			.getLogger(LoginMessageHandler.class);

	/** The login validated. */
	boolean loginValidated = false;

	/** The message recieved. */
	boolean messageRecieved = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gc.irc.common.api.IIRCMessageHandler#handle(com.gc.irc.common.protocol
	 * .IRCMessage)
	 */
	public void handle(final IRCMessage message) {
		LOGGER.info("Message recived: " + message);
		if (message instanceof IRCMessageNoticeLogin) {
			login = ((IRCMessageNoticeLogin) message).getUser();
			if (login != null) {
				loginValidated = true;
				LOGGER.info("USER : " + login.toStringXML(""));
			}
		} else if (message instanceof IRCMessageNoticeRegister) {
			login = ((IRCMessageNoticeRegister) message).getUser();
			if (login != null) {
				loginValidated = true;
				LOGGER.info("REGISTER USER : " + login.toStringXML(""));
			}
		}
		messageRecieved = true;
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
	 * Checks if is message recieved.
	 * 
	 * @return true, if is message recieved
	 */
	public boolean isMessageRecieved() {
		return messageRecieved;
	}

	/**
	 * Reset.
	 */
	public void reset() {
		LOGGER.info("Reset");
		messageRecieved = false;
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
