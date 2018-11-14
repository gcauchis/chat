package com.gc.irc.common.exception.security;

/**
 * The Class IRCInvalideSenderException.
 *
 * @version 0.0.4
 * @author x472511
 */
public class InvalidSenderException extends SecurityException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3690981509325343624L;

    /**
     * Instantiates a new iRC invalide sender exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public InvalidSenderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new iRC invalide sender exception.
     *
     * @param message
     *            the message
     */
    public InvalidSenderException(final String message) {
        super(message);
    }

}
