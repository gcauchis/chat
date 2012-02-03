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

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString() {
	    final String TAB = " ";
	
	    StringBuilder retValue = new StringBuilder();
	    
	    retValue.append("IRCMessageNoticeServerMessage ( ")
	        .append(super.toString()).append(TAB)
	        .append("message = ").append(this.message).append(TAB)
	        .append(" )");
	    
	    return retValue.toString();
	}
}
