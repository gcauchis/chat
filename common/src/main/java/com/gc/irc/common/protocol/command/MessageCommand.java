package com.gc.irc.common.protocol.command;

import com.gc.irc.common.protocol.Message;

/**
 * The Class IRCMessageCommand.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public class MessageCommand extends Message {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3322674812721397858L;

    /**
     * Instantiates a new iRC message command.
     *
     * @param userId
     *            the user id
     */
    protected MessageCommand(final long userId) {
        super(userId);
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
        builder.append("IRCMessageCommand [").append(super.toString()).append(", ]");
        return builder.toString();
    }

}
