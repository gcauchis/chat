package com.gc.irc.common.protocol.chat;

import java.util.List;

import com.gc.irc.common.message.ClientMessageLine;

/**
 * The Class IRCMessageChatPrivate.
 *
 * @version 0.0.4
 * @author x472511
 */
public class MessageChatPrivate extends MessageChat {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6494361960510331113L;

    /** The cpt persist. */
    private int cptPersist = 0;

    /** The to id. */
    private long toId = -1;

    /**
     * Instantiates a new iRC message chat private.
     *
     * @param userID
     *            the user id
     * @param lines
     *            the lines
     */
    public MessageChatPrivate(final long userID, final List<ClientMessageLine> lines) {
        super(userID, lines);
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
     */
    public MessageChatPrivate(final long userID, final List<ClientMessageLine> lines, final long toId) {
        this(userID, lines);
        this.toId = toId;
    }

    /**
     * Gets the to id.
     *
     * @return the to id
     */
    public long getToId() {
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
     * @see java.lang.Object#toString()
     */
    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IRCMessageChatPrivate [").append(super.toString()).append(", cptPersist=").append(cptPersist).append(", toId=").append(toId)
                .append("]");
        return builder.toString();
    }

}
