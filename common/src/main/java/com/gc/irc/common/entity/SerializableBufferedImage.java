package com.gc.irc.common.entity;

import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.slf4j.Logger;

import com.gc.irc.common.utils.LoggerUtils;

/**
 * The Class SerializableBufferedImage.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public class SerializableBufferedImage implements Serializable {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerUtils.getLogger(SerializableBufferedImage.class);

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The byte image. */
    private byte[] byteImage = null;

    /**
     * Instantiates a new serializable buffered image.
     *
     * @param bufferedImage
     *            the buffered image
     */
    public SerializableBufferedImage(final BufferedImage bufferedImage) {
        byteImage = toByteArray(bufferedImage);
    }
    
    /**
     * <p>Constructor for SerializableBufferedImage.</p>
     *
     * @param byteImage an array of byte.
     */
    public SerializableBufferedImage(final byte[] byteImage) {
        this.byteImage = byteImage;
    }

    /**
     * From byte array.
     * 
     * @param imagebytes
     *            the imagebytes
     * @return the buffered image
     */
    private BufferedImage fromByteArray(final byte[] imagebytes) {
        try {
            if (imagebytes != null && (imagebytes.length > 0)) {
                return ImageIO.read(new ByteArrayInputStream(imagebytes));
            }
            return null;
        } catch (final IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Gets the buffered image.
     *
     * @return the buffered image
     */
    public BufferedImage getBufferedImage() {
        LOGGER.debug(byteImage.length / Math.pow(2, 10) + " ko");
        return fromByteArray(byteImage);
    }

    /**
     * To byte array.
     * 
     * @param bufferedImage
     *            the buffered image
     * @return the byte[]
     */
    private byte[] toByteArray(final BufferedImage bufferedImage) {
        if (bufferedImage != null) {
            final BufferedImage image = bufferedImage;
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                if (bufferedImage.getTransparency() == Transparency.TRANSLUCENT) {
                    ImageIO.write(image, "png", baos);
                } else {
                    ImageIO.write(image, "jpg", baos);
                }
            } catch (final IOException e) {
                throw new IllegalStateException(e.toString());
            }
            return baos.toByteArray();
        }
        return new byte[0];
    }

	/**
	 * <p>Getter for the field <code>byteImage</code>.</p>
	 *
	 * @return an array of byte.
	 */
	public byte[] getByteImage() {
		return Arrays.copyOf(byteImage, byteImage.length);
	}

	/**
	 * <p>Setter for the field <code>byteImage</code>.</p>
	 *
	 * @param byteImage an array of byte.
	 */
	public void setByteImage(byte[] byteImage) {
		this.byteImage = Arrays.copyOf(byteImage, byteImage.length);
	}
}
