package com.gc.irc.server.exception;

/**
 * Special Exception for the IRCServer.
 * @author gcauchis
 *
 */
public class IRCServerException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6210212249602883652L;

	/**
	 * Instantiates a new iRC server exception.
	 *
	 * @param e the e
	 */
	public IRCServerException(Exception e) {
		super(e);
	}

}
