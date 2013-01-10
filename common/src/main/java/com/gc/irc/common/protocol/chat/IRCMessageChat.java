package com.gc.irc.common.protocol.chat;

import java.util.List;

import com.gc.irc.common.message.api.IClientMessageLine;
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

    /** The chat message type. */
    private IRCMessageChatType chatMessageType = IRCMessageChatType.GLOBAL;

    /** The lines. */
    private List<IClientMessageLine> lines;

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
    public IRCMessageChat(final int userID, final List<IClientMessageLine> lines) {
        super(userID, IRCMessageType.CHATMESSAGE);
        this.lines = lines;
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
     * Sets the lines.
     * 
     * @param lines
     *            the new lines
     */
    public void setLines(final List<IClientMessageLine> lines) {
        this.lines = lines;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.common.protocol.IRCMessage#toString()
     */
    @Override
    public String toString() {
        return "IRCMessageChat(" + super.toString() + ") [chatMessageType=" + chatMessageType + ", lines=" + lines + "]";
    }

}
