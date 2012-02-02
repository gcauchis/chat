package com.gc.irc.common.protocol.chat;

import java.util.ArrayList;
import java.util.Arrays;

import com.gc.irc.common.api.ITextElement;
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
	private ArrayList<ITextElement> lines;

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
			final ArrayList<ITextElement> lines, final float[] textColor) {
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
	public void setLines(final ArrayList<ITextElement> lines) {
		this.lines = lines;
	}

	/**
	 * Gets the lines.
	 * 
	 * @return the lines
	 */
	public ArrayList<ITextElement> getLines() {
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

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("IRCMessageChat [lines=").append(lines)
				.append(", chatMessageType=").append(chatMessageType)
				.append(", textColor=").append(Arrays.toString(textColor))
				.append("]");
		return builder.toString();
	}

}
