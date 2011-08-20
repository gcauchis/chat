package com.gc.irc.common.protocol.chat;

import java.util.ArrayList;

import com.gc.irc.common.api.ITextAreaLine;
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
	private ArrayList<ITextAreaLine> lines;
	
	/** The chat message type. */
	private IRCMessageChatType chatMessageType = IRCMessageChatType.GLOBAL;
	
	/** The text color. */
	private float[] textColor; //the RGB components of the text color
	
	/**
	 * Instantiates a new iRC message chat.
	 *
	 * @param userID the user id
	 * @param lines the lines
	 * @param textColor the text color
	 */
	public IRCMessageChat(int userID, ArrayList<ITextAreaLine> lines, float[] textColor){
		super(userID,IRCMessageType.CHATMESSAGE);
		this.setLines(lines);
		this.setTextColor(textColor);
	}

	/**
	 * Sets the lines.
	 *
	 * @param lines the new lines
	 */
	public void setLines(ArrayList<ITextAreaLine> lines) {
		this.lines = lines;
	}

	/**
	 * Gets the lines.
	 *
	 * @return the lines
	 */
	public ArrayList<ITextAreaLine> getLines() {
		return lines;
	}

	/**
	 * Sets the chat message type.
	 *
	 * @param chatMessageType the new chat message type
	 */
	protected void setChatMessageType(IRCMessageChatType chatMessageType) {
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
	 * @param textColor the new text color
	 */
	public void setTextColor(float[] textColor) {
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
	
}
