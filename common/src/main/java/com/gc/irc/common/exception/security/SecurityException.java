package com.gc.irc.common.exception.security;

import com.gc.irc.common.exception.IRCRuntimeException;

/**
 * The Class IRCSecurityException.
 */
public class SecurityException extends IRCRuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1938248536012113269L;

    /**
     * Instantiates a new iRC security exception.
     * 
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public SecurityException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new iRC security exception.
     * 
     * @param message
     *            the message
     */
    public SecurityException(final String message) {
        super(message);
    }

}