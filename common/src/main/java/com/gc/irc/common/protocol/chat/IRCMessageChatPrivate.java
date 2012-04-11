package com.gc.irc.common.protocol.chat;

import java.util.ArrayList;

import com.gc.irc.common.message.api.IClientMessageLine;

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
			final ArrayList<IClientMessageLine> lines) {
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
			final ArrayList<IClientMessageLine> lines, final int toId) {
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

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString() {
	    final String TAB = " ";
	
	    StringBuilder retValue = new StringBuilder();
	    
	    retValue.append("IRCMessageChatPrivate ( ")
	        .append(super.toString()).append(TAB)
	        .append("toId = ").append(this.toId).append(TAB)
	        .append("cptPersist = ").append(this.cptPersist).append(TAB)
	        .append(" )");
	    
	    return retValue.toString();
	}

}
