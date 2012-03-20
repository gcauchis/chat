package com.acp.common.crypto.exception;

/**
 * The Class EncoderException.
 */
public class EncoderException
        extends Exception {

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
     * @param message the message
     * @param cause the cause
     */
    public EncoderException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new encryption exception.
     * 
     * @param message the message
     */
    public EncoderException(String message) {
        super(message);
    }

    /**
     * Instantiates a new encryption exception.
     * 
     * @param cause the cause
     */
    public EncoderException(Throwable cause) {
        super(cause);
    }

}
