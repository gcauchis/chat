package com.gc.irc.common.protocol;

import java.io.Serializable;

/**
 * Object used to communicate between the Client and the Server.
 * 
 * @author Colin, Gabriel
 */
public class IRCMessage implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -60278983013467149L;

	/** The from id. */
	private int fromId; // the id of the user who sends the message

	/** The type. */
	private IRCMessageType type; // the type of the message

	/**
	 * Instantiates a new iRC message.
	 * 
	 * @param fromId
	 *            the from id
	 * @param type
	 *            the type
	 */
	public IRCMessage(final int fromId, final IRCMessageType type) {
		this.fromId = fromId;
		this.type = type;
	}

	/**
	 * Gets the from id.
	 * 
	 * @return the from id
	 */
	public int getFromId() {
		return fromId;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public IRCMessageType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	protected void setType(final IRCMessageType type) {
		this.type = type;
	}

	/**
	 * Sets the from id.
	 * 
	 * @param fromId
	 *            the new from id
	 */
	protected void setFromId(final int fromId) {
		this.fromId = fromId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IRCMessage(" + super.toString() + ") [fromId=" + fromId
				+ ", type=" + type + "]";
	}
}
