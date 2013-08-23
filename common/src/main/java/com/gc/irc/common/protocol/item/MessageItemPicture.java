package com.gc.irc.common.protocol.item;

import java.awt.image.BufferedImage;

import com.gc.irc.common.entity.SerializableBufferedImage;
import com.gc.irc.common.protocol.Message;

/**
 * Message use to send picture.
 * 
 * @author gcauchis
 * 
 */
public class MessageItemPicture extends Message {

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
    public MessageItemPicture(final long userId, final BufferedImage image) {
        super(userId);
        imageData = new SerializableBufferedImage(image);
    }
    
    public MessageItemPicture(final long userId, final SerializableBufferedImage image) {
        super(userId);
        imageData = image;
    }

    /**
     * Gets the image data.
     * 
     * @return the image data
     */
    public BufferedImage getImageData() {
        return imageData.getBufferedImage();
    }
    
    public SerializableBufferedImage getImageBuffer() {
        return imageData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IRCMessageItemPicture [").append(super.toString()).append(", imageData=").append(imageData).append("]");
        return builder.toString();
    }
}
