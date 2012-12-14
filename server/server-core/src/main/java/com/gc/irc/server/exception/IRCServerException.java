package com.gc.irc.server.exception;

import com.gc.irc.common.exception.IRCException;

/**
 * Special Exception for the IRCServer.
 * 
 * @author gcauchis
 * 
 */
public class IRCServerException extends IRCException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6210212249602883652L;

    /**
     * Instantiates a new iRC server exception.
     */
    public IRCServerException() {
        super();
    }

    /**
     * Instantiates a new iRC server exception.
     * 
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public IRCServerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new iRC server exception.
     * 
     * @param message
     *            the message
     */
    public IRCServerException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new iRC server exception.
     * 
     * @param cause
     *            the cause
     */
    public IRCServerException(final Throwable cause) {
        super(cause);
    }

}
