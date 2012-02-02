package com.gc.irc.common.protocol.notice;

/**
 * Notification used to send a message from the server.
 * 
 * @author gcauchis
 * 
 */
public class IRCMessageNoticeServerMessage extends IRCMessageNotice {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9085335438335989213L;

	/** The message. */
	private String message;

	/**
	 * Instantiates a new iRC message notice server message.
	 */
	private IRCMessageNoticeServerMessage() {
		super(IRCMessageNoticeType.SERVER_MESSAGE);
	}

	/**
	 * Instantiates a new iRC message notice server message.
	 * 
	 * @param message
	 *            the message
	 */
	public IRCMessageNoticeServerMessage(final String message) {
		this();
		this.message = message;
	}

	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("IRCMessageNoticeServerMessage [message=")
				.append(message).append("]");
		return builder.toString();
	}
}
