package com.gc.irc.common.exception.utils;

import com.gc.irc.common.exception.IRCException;

/**
 * The Class XMLException.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public class XMLException extends IRCException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7356551355423816858L;

    /**
     * Instantiates a new xML exception.
     */
    public XMLException() {
        super();
    }

    /**
     * Instantiates a new xML exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public XMLException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new xML exception.
     *
     * @param message
     *            the message
     */
    public XMLException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new xML exception.
     *
     * @param cause
     *            the cause
     */
    public XMLException(final Throwable cause) {
        super(cause);
    }

}
