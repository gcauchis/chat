package com.gc.irc.common.exception;

/**
 * The Class IRCException.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public class IRCException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -134865648470266538L;

    /**
     * Instantiates a new iRC exception.
     */
    public IRCException() {
        super();
    }

    /**
     * Instantiates a new iRC exception.
     *
     * @param message
     *            the message
     */
    public IRCException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new iRC exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public IRCException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new iRC exception.
     *
     * @param cause
     *            the cause
     */
    public IRCException(final Throwable cause) {
        super(cause);
    }

}
