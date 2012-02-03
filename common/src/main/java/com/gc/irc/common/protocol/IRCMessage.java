package com.gc.irc.common.protocol;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import org.apache.log4j.Logger;

/**
 * Object used to communicate between the Client and the Server.
 * 
 * @author Colin, Gabriel
 */
public class IRCMessage implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -60278983013467149L;

	/** The Constant logger. */
	private static final Logger LOGGER = Logger.getLogger(IRCMessage.class);

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

	/**
	 * Send the message.
	 * 
	 * @param outObject
	 *            Stream where is send the message
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void envoyerMessageObjetSocket(final ObjectOutputStream outObject)
			throws IOException {
		LOGGER.debug("Send the Message.");
		outObject.writeObject(this);
		outObject.flush();
		outObject.reset();
	}

	/**
	 * Wait and receive a Message.
	 * 
	 * @param inObject
	 *            Stream to listen.
	 * @return Message Received message.
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @throws InvalidClassException
	 *             the invalid class exception
	 * @throws StreamCorruptedException
	 *             the stream corrupted exception
	 * @throws OptionalDataException
	 *             the optional data exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static IRCMessage recevoirMessageObjetSocket(
			final ObjectInputStream inObject) throws ClassNotFoundException,
			InvalidClassException, StreamCorruptedException,
			OptionalDataException, IOException {

		LOGGER.debug("Wait for a message in the Stream.");
		final IRCMessage message = (IRCMessage) inObject.readObject();
		LOGGER.debug("Message receive.");

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
	    
	    retValue.append("IRCMessage ( ")
	        .append(super.toString()).append(TAB)
	        .append("fromId = ").append(this.fromId).append(TAB)
	        .append("type = ").append(this.type).append(TAB)
	        .append(" )");
	    
	    return retValue.toString();
	}
}
