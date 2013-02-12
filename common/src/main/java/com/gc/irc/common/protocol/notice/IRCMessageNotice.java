package com.gc.irc.common.protocol.notice;

import com.gc.irc.common.protocol.IRCMessage;

/**
 * Represent notification message.
 * 
 * @author gcauchis
 * 
 */
public class IRCMessageNotice extends IRCMessage {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7286654418377130362L;

    /**
     * Instantiates a new iRC message notice.
     */
    public IRCMessageNotice() {
        super(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IRCMessageNotice [").append(super.toString()).append(", ]");
        return builder.toString();
    }
}
