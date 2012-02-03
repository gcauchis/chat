package com.gc.irc.common.protocol.item;

import java.awt.image.BufferedImage;

import com.gc.irc.common.entity.SerializableBufferedImage;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.IRCMessageType;

/**
 * Message use to send picture.
 * 
 * @author gcauchis
 * 
 */
public class IRCMessageItemPicture extends IRCMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4751207637000120876L;

	/** The image data. */
	private SerializableBufferedImage imageData;

	/**
	 * Instantiates a new iRC message user picture.
	 * 
	 * @param userId
	 *            the user id
	 * @param image
	 *            the image
	 */
	public IRCMessageItemPicture(final int userId, final BufferedImage image) {
		super(userId, IRCMessageType.ITEM);
		imageData = new SerializableBufferedImage(image);
	}

	/**
	 * Gets the image data.
	 * 
	 * @return the image data
	 */
	public BufferedImage getImageData() {
		return imageData.getBufferedImage();
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
	    
	    retValue.append("IRCMessageItemPicture ( ")
	        .append(super.toString()).append(TAB)
	        .append("imageData = ").append(this.imageData).append(TAB)
	        .append(" )");
	    
	    return retValue.toString();
	}
}
