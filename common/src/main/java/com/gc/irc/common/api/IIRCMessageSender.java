package com.gc.irc.common.api;

import com.gc.irc.common.protocol.IRCMessage;

/**
 * The Interface IIRCMessageSender.
 */
public interface IIRCMessageSender {

	/**
	 * Send.
	 * 
	 * @param message
	 *            the message
	 */
	void send(IRCMessage message);
}
