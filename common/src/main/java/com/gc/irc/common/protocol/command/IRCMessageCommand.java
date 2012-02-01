package com.gc.irc.common.protocol.command;

import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.IRCMessageType;

/**
 * The Class IRCMessageCommand.
 */
public class IRCMessageCommand extends IRCMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3322674812721397858L;
	
	/** The command type. */
	private IRCMessageCommandType commandType;

	/**
	 * Instantiates a new iRC message command.
	 *
	 * @param userId the user id
	 */
	private IRCMessageCommand(int userId) {
		super(userId, IRCMessageType.COMMAND);
	}

	/**
	 * Instantiates a new iRC message command.
	 *
	 * @param userId the user id
	 * @param commandType the command type
	 */
	public IRCMessageCommand(int userId, IRCMessageCommandType commandType) {
		this(userId);
		this.commandType = commandType;
	}
	
	/**
	 * Gets the command type.
	 *
	 * @return the command type
	 */
	public IRCMessageCommandType getCommandType() {
		return commandType;
	}

}