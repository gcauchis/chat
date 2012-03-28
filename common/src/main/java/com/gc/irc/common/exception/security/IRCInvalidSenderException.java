package com.gc.irc.common.exception.security;

/**
 * The Class IRCInvalideSenderException.
 */
public class IRCInvalidSenderException extends IRCSecurityException {

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
    public IRCInvalidSenderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new iRC invalide sender exception.
     * 
     * @param message
     *            the message
     */
    public IRCInvalidSenderException(final String message) {
        super(message);
    }

}
