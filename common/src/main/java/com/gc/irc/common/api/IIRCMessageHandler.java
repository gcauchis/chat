package com.gc.irc.common.api;

import com.gc.irc.common.protocol.IRCMessage;

/**
 * The Interface IMessageHandler.
 */
public interface IIRCMessageHandler {

	/**
	 * Handle.
	 * 
	 * @param message
	 *            the message
	 */
	void handle(IRCMessage message);
}
