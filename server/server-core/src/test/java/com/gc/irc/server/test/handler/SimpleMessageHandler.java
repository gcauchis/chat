package com.gc.irc.server.test.handler;

import com.gc.irc.common.protocol.IRCMessage;

/**
 * The Class SimpleMessageHandler.
 */
public class SimpleMessageHandler extends AbstractMessageHandler {
	
	/** The last received message. */
	private IRCMessage lastReceivedMessage;

	/**
	 * Gets the last received message.
	 *
	 * @return the last received message
	 */
	public IRCMessage getLastReceivedMessage() {
		return lastReceivedMessage;
	}

	/* (non-Javadoc)
	 * @see com.gc.irc.server.test.handler.AbstractMessageHandler#handleInternal(com.gc.irc.common.protocol.IRCMessage)
	 */
	@Override
	protected void handleInternal(IRCMessage message) {
		lastReceivedMessage = message;
	}

	/* (non-Javadoc)
	 * @see com.gc.irc.server.test.handler.AbstractMessageHandler#resetInsernal()
	 */
	@Override
	protected void resetInsernal() {
		lastReceivedMessage = null;
	}

}
