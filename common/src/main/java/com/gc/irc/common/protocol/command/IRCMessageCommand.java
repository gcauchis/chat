package com.gc.irc.common.protocol.command;

import com.gc.irc.common.protocol.IRCMessage;

/**
 * The Class IRCMessageCommand.
 */
public class IRCMessageCommand extends IRCMessage {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3322674812721397858L;

    /**
     * Instantiates a new iRC message command.
     * 
     * @param userId
     *            the user id
     */
    protected IRCMessageCommand(final int userId) {
        super(userId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IRCMessageCommand [").append(super.toString()).append(", ]");
        return builder.toString();
    }

}
