package com.gc.irc.common.protocol.chat;

import java.util.ArrayList;
import java.util.Arrays;

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
	private ArrayList<IClientMessageLine> lines;

	/** The chat message type. */
	private IRCMessageChatType chatMessageType = IRCMessageChatType.GLOBAL;

	/** The text color. */
	private float[] textColor; // the RGB components of the text color

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
			final ArrayList<IClientMessageLine> lines, final float[] textColor) {
		super(userID, IRCMessageType.CHATMESSAGE);
		setLines(lines);
		setTextColor(textColor);
	}

	/**
	 * Sets the lines.
	 * 
	 * @param lines
	 *            the new lines
	 */
	public void setLines(final ArrayList<IClientMessageLine> lines) {
		this.lines = lines;
	}

	/**
	 * Gets the lines.
	 * 
	 * @return the lines
	 */
	public ArrayList<IClientMessageLine> getLines() {
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
	 * Sets the text color.
	 * 
	 * @param textColor
	 *            the new text color
	 */
	public void setTextColor(final float[] textColor) {
		this.textColor = textColor;
	}

	/**
	 * Gets the text color.
	 * 
	 * @return the text color
	 */
	public float[] getTextColor() {
		return textColor;
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
	        .append("textColor = ").append(this.textColor).append(TAB)
	        .append(" )");
	    
	    return retValue.toString();
	}

}
