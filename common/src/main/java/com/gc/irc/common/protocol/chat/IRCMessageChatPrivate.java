package com.gc.irc.common.protocol.chat;

import java.util.ArrayList;

import com.gc.irc.common.api.ITextElement;

/**
 * The Class IRCMessageChatPrivate.
 */
public class IRCMessageChatPrivate extends IRCMessageChat {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6494361960510331113L;

	/** The to id. */
	private int toId = -1;

	/** The cpt persist. */
	private int cptPersist = 0;

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
			final ArrayList<ITextElement> lines, final float[] textColor) {
		super(userID, lines, textColor);
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
			final ArrayList<ITextElement> lines, final int toId,
			final float[] textColor) {
		this(userID, lines, textColor);
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

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("IRCMessageChatPrivate [toId=").append(toId)
				.append(", cptPersist=").append(cptPersist).append("]");
		return builder.toString();
	}

	/**
	 * Get the number of passage in the handler.
	 * 
	 * @return the int
	 */
	public int numPassage() {
		return cptPersist++;
	}

}
