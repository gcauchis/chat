package com.gc.irc.common.protocol.chat;

import java.util.List;

import com.gc.irc.common.message.api.IClientMessageLine;

/**
 * The Class IRCMessageChatPrivate.
 */
public class IRCMessageChatPrivate extends IRCMessageChat {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6494361960510331113L;

	/** The cpt persist. */
	private int cptPersist = 0;

	/** The to id. */
	private int toId = -1;

	/**
	 * Instantiates a new iRC message chat private.
	 * 
	 * @param userID
	 *            the user id
	 * @param lines
	 *            the lines
	 * @param textColor
	 *            the text color
	 */
	public IRCMessageChatPrivate(final int userID,
			final List<IClientMessageLine> lines) {
		super(userID, lines);
		setChatMessageType(IRCMessageChatType.PRIVATE);
	}

	/**
	 * Instantiates a new iRC message chat private.
	 * 
	 * @param userID
	 *            the user id
	 * @param lines
	 *            the lines
	 * @param toId
	 *            the to id
	 * @param textColor
	 *            the text color
	 */
	public IRCMessageChatPrivate(final int userID,
			final List<IClientMessageLine> lines, final int toId) {
		this(userID, lines);
		this.toId = toId;
	}

	/**
	 * Gets the to id.
	 * 
	 * @return the to id
	 */
	public int getToId() {
		return toId;
	}

	/**
	 * Get the number of passage in the handler.
	 * 
	 * @return the int
	 */
	public int numPassage() {
		return cptPersist++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gc.irc.common.protocol.chat.IRCMessageChat#toString()
	 */
	@Override
	public String toString() {
		return "IRCMessageChatPrivate(" + super.toString() + ") [cptPersist="
				+ cptPersist + ", toId=" + toId + "]";
	}

}
