package com.gc.irc.server.test.handler;

import org.apache.log4j.Logger;

import com.gc.irc.common.api.IIRCMessageHandler;
import com.gc.irc.common.protocol.IRCMessage;

/**
 * The Class AbstractMessageHandler.
 */
public abstract class AbstractMessageHandler implements IIRCMessageHandler {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger
			.getLogger(AbstractMessageHandler.class);
	/** The message recieved. */
	boolean messageRecieved = false;

	/**
	 * Instantiates a new abstract message handler.
	 */
	public AbstractMessageHandler() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.gc.irc.common.api.IIRCMessageHandler#handle(com.gc.irc.common.protocol.IRCMessage)
	 */
	public void handle(final IRCMessage message) {
		LOGGER.info("Message recived: " + message);
		handleInternal(message);
		messageRecieved = true;
	}

	/**
	 * Handle internal.
	 * 
	 * @param message
	 *            the message
	 */
	protected abstract void handleInternal(final IRCMessage message);

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
		resetInsernal();
	}

	/**
	 * Reset insernal.
	 */
	protected abstract void resetInsernal();

}