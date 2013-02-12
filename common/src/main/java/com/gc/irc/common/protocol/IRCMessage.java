package com.gc.irc.common.protocol;

import java.io.Serializable;

/**
 * Object used to communicate between the Client and the Server.
 * 
 * @author Colin, Gabriel
 */
public class IRCMessage implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -60278983013467149L;

    /** The from id. */
    private int fromId; // the id of the user who sends the message

    /**
     * Instantiates a new iRC message.
     * 
     * @param fromId
     *            the from id
     * @param type
     *            the type
     */
    public IRCMessage(final int fromId) {
        this.fromId = fromId;
    }

    /**
     * Gets the from id.
     * 
     * @return the from id
     */
    public int getFromId() {
        return fromId;
    }

    /**
     * Sets the from id.
     * 
     * @param fromId
     *            the new from id
     */
    protected void setFromId(final int fromId) {
        this.fromId = fromId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IRCMessage [").append(super.toString()).append(", fromId=").append(fromId).append("]");
        return builder.toString();
    }
}
