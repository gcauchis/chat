package com.gc.irc.common.protocol.chat;

import java.util.List;

import com.gc.irc.common.message.api.IClientMessageLine;
import com.gc.irc.common.protocol.IRCMessage;

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
        super(userID);
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
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IRCMessageChat [").append(super.toString()).append(", lines=").append(lines).append("]");
        return builder.toString();
    }

}
