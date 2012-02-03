package com.gc.irc.common.api.impl;

import com.gc.irc.common.api.IClientMessageLine;

/**
 * The Class BasicClientMessage.
 */
public class BasicClientMessage implements IClientMessageLine {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6741145004079482180L;
	
	/** The message. */
	private String message;

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
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
	    
	    retValue.append("BasicClientMessage ( ")
	        .append(super.toString()).append(TAB)
	        .append("message = ").append(this.message).append(TAB)
	        .append(" )");
	    
	    return retValue.toString();
	}
	
}
