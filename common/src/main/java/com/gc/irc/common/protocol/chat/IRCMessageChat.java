package com.gc.irc.common.protocol.chat;

import java.util.List;

import com.gc.irc.common.api.IClientMessageLine;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.IRCMessageType;

/**
 * The seri.
 * 
 * @author Colin
 */
public class IRCMessageChat extends IRCMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6599297905344621111L;

	/** The lines. */
	private List<IClientMessageLine> lines;

	/** The chat message type. */
	private IRCMessageChatType chatMessageType = IRCMessageChatType.GLOBAL;

	/**
	 * Instantiates a new iRC message chat.
	 * 
	 * @param userID
	 *            the user id
	 * @param lines
	 *            the lines
	 * @param textColor
	 *            the text color
	 */
	public IRCMessageChat(final int userID,
			final List<IClientMessageLine> lines) {
		super(userID, IRCMessageType.CHATMESSAGE);
		setLines(lines);
	}

	/**
	 * Sets the lines.
	 * 
	 * @param lines
	 *            the new lines
	 */
	public void setLines(final List<IClientMessageLine> lines) {
		this.lines = lines;
	}

	/**
	 * Gets the lines.
	 * 
	 * @return the lines
	 */
	public List<IClientMessageLine> getLines() {
		return lines;
	}

	/**
	 * Sets the chat message type.
	 * 
	 * @param chatMessageType
	 *            the new chat message type
	 */
	protected void setChatMessageType(final IRCMessageChatType chatMessageType) {
		this.chatMessageType = chatMessageType;
	}

	/**
	 * Gets the chat message type.
	 * 
	 * @return the chat message type
	 */
	public IRCMessageChatType getChatMessageType() {
		return chatMessageType;
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
	    
	    retValue.append("IRCMessageChat ( ")
	        .append(super.toString()).append(TAB)
	        .append("lines = ").append(this.lines).append(TAB)
	        .append("chatMessageType = ").append(this.chatMessageType).append(TAB)
	        .append(" )");
	    
	    return retValue.toString();
	}

}
