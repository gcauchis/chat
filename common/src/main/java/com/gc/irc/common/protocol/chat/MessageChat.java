package com.gc.irc.common.protocol.chat;

import java.util.List;

import com.gc.irc.common.message.ClientMessageLine;
import com.gc.irc.common.protocol.Message;

/**
 * The seri.
 *
 * @version 0.0.4
 * @author x472511
 */
public class MessageChat extends Message {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6599297905344621111L;

    /** The lines. */
    private List<ClientMessageLine> lines;

    /**
     * Instantiates a new iRC message chat.
     *
     * @param userID
     *            the user id
     * @param lines
     *            the lines
     */
    public MessageChat(final long userID, final List<ClientMessageLine> lines) {
        super(userID);
        this.lines = lines;
    }

    /**
     * Gets the lines.
     *
     * @return the lines
     */
    public List<ClientMessageLine> getLines() {
        return lines;
    }

    /**
     * Sets the lines.
     *
     * @param lines
     *            the new lines
     */
    public void setLines(final List<ClientMessageLine> lines) {
        this.lines = lines;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IRCMessageChat [").append(super.toString()).append(", lines=").append(lines).append("]");
        return builder.toString();
    }

}
