package com.gc.irc.server.bridge;

import com.gc.irc.common.exception.IRCException;

/**
 * The Class ServerBridgeException.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public class ServerBridgeException extends IRCException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4296045901769966809L;

    /**
     * The Constructor.
     */
    public ServerBridgeException() {
        super();
    }

    /**
     * The Constructor.
     *
     * @param message
     *            the message
     */
    public ServerBridgeException(final String message) {
        super(message);
    }

    /**
     * The Constructor.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public ServerBridgeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * The Constructor.
     *
     * @param cause
     *            the cause
     */
    public ServerBridgeException(final Throwable cause) {
        super(cause);
    }

}
