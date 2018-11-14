package com.gc.irc.common.exception;

/**
 * The Class IRCRuntimeException.
 *
 * @version 0.0.4
 * @author x472511
 */
public class IRCRuntimeException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5941741054377561827L;

    /**
     * Instantiates a new iRC runtime exception.
     */
    public IRCRuntimeException() {
        super();
    }

    /**
     * Instantiates a new iRC runtime exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public IRCRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new iRC runtime exception.
     *
     * @param message
     *            the message
     */
    public IRCRuntimeException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new iRC runtime exception.
     *
     * @param cause
     *            the cause
     */
    public IRCRuntimeException(final Throwable cause) {
        super(cause);
    }

}
