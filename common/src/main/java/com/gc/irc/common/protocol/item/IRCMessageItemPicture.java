package com.gc.irc.common.protocol.item;

import java.awt.image.BufferedImage;

import com.gc.irc.common.entity.SerializableBufferedImage;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.IRCMessageType;

/**
 * Message use to send picture.
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
	 * @param userId the user id
	 * @param image the image
	 */
	public IRCMessageItemPicture(int userId, BufferedImage image) {
		super(userId, IRCMessageType.ITEM);
		imageData = new SerializableBufferedImage(image);
	}

	/**
	 * Gets the image data.
	 *
	 * @return the image data
	 */
	public BufferedImage getImageData(){
		return imageData.getBufferedImage();
	}
}
