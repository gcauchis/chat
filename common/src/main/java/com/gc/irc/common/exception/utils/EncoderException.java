package com.gc.irc.common.exception.utils;

import com.gc.irc.common.exception.IRCException;

/**
 * The Class EncoderException.
 */
public class EncoderException extends IRCException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7985190218244364512L;

    /**
     * Instantiates a new encryption exception.
     */
    public EncoderException() {
        super();
    }

    /**
     * Instantiates a new encryption exception.
     * 
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public EncoderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new encryption exception.
     * 
     * @param message
     *            the message
     */
    public EncoderException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new encryption exception.
     * 
     * @param cause
     *            the cause
     */
    public EncoderException(final Throwable cause) {
        super(cause);
    }

}
