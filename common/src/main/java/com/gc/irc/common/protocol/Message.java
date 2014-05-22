package com.gc.irc.common.protocol;

import java.io.Serializable;

/**
 * Object used to communicate between the Client and the Server.
 *
 * @version 0.0.4
 * @author a870560
 */
public class Message implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -60278983013467149L;

    /** The from id. */
    private long fromId; // the id of the user who sends the message

    /**
     * Instantiates a new iRC message.
     *
     * @param fromId
     *            the from id
     */
    public Message(final long fromId) {
        this.fromId = fromId;
    }

    /**
     * Gets the from id.
     *
     * @return the from id
     */
    public long getFromId() {
        return fromId;
    }

    /**
     * Sets the from id.
     *
     * @param fromId
     *            the new from id
     */
    protected void setFromId(final long fromId) {
        this.fromId = fromId;
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
        builder.append("IRCMessage [").append(super.toString()).append(", fromId=").append(fromId).append("]");
        return builder.toString();
    }
}
