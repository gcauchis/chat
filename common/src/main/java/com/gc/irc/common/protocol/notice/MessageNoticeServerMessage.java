package com.gc.irc.common.protocol.notice;

/**
 * Notification used to send a message from the server.
 *
 * @version 0.0.4
 * @author x472511
 */
public class MessageNoticeServerMessage extends MessageNotice {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 9085335438335989213L;

    /** The message. */
    private String message;

    /**
     * Instantiates a new iRC message notice server message.
     *
     * @param message
     *            the message
     */
    public MessageNoticeServerMessage(final String message) {
        this.message = message;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
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
        builder.append("IRCMessageNoticeServerMessage [").append(super.toString()).append(", message=").append(message).append("]");
        return builder.toString();
    }
}
