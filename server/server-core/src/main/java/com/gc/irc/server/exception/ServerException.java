package com.gc.irc.server.exception;

import com.gc.irc.common.exception.IRCException;

/**
 * Special Exception for the IRCServer.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public class ServerException extends IRCException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6210212249602883652L;

    /**
     * Instantiates a new iRC server exception.
     */
    public ServerException() {
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
    public ServerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new iRC server exception.
     *
     * @param message
     *            the message
     */
    public ServerException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new iRC server exception.
     *
     * @param cause
     *            the cause
     */
    public ServerException(final Throwable cause) {
        super(cause);
    }

}
