package com.gc.irc.server.test.protocol.command;

import com.gc.irc.common.protocol.command.MessageCommand;

/**
 * <p>MessageCommandTestDeleteUser class.</p>
 *
 * @author gcauchis
 * @version 0.0.4
 * @since 0.0.4
 */
public class MessageCommandTestDeleteUser extends MessageCommand {

	/**
	 * <p>Constructor for MessageCommandTestDeleteUser.</p>
	 *
	 * @param userId a long.
	 */
	public MessageCommandTestDeleteUser(long userId) {
		super(userId);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -211219261601382941L;

}
