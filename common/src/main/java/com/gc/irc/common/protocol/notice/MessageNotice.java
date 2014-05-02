package com.gc.irc.common.protocol.notice;

import com.gc.irc.common.protocol.Message;

/**
 * Represent notification message.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public class MessageNotice extends Message {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7286654418377130362L;

    /**
     * Instantiates a new iRC message notice.
     */
    public MessageNotice() {
        super(0);
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
        builder.append("IRCMessageNotice [").append(super.toString()).append(", ]");
        return builder.toString();
    }
}
