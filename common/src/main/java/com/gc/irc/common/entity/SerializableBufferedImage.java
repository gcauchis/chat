package com.gc.irc.common.entity;


import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
 
import javax.imageio.ImageIO;
 
/**
 * The Class SerializableBufferedImage.
 */
public class SerializableBufferedImage implements Serializable {
 
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The byte image. */
	private byte[] byteImage = null;
 
	/**
	 * Instantiates a new serializable buffered image.
	 *
	 * @param bufferedImage the buffered image
	 */
	public SerializableBufferedImage(BufferedImage bufferedImage) {
		this.byteImage = toByteArray(bufferedImage);
	}
 
	/**
	 * Gets the buffered image.
	 *
	 * @return the buffered image
	 */
	public BufferedImage getBufferedImage() {
		System.out.println(byteImage.length/Math.pow(2,10)+" ko");
		return fromByteArray(byteImage);
	}
 
	/**
	 * From byte array.
	 *
	 * @param imagebytes the imagebytes
	 * @return the buffered image
	 */
	private BufferedImage fromByteArray(byte[] imagebytes) {
		try {
			if (imagebytes != null && (imagebytes.length > 0)) {
				BufferedImage im = ImageIO.read(new ByteArrayInputStream(imagebytes));
				return im;
			}
			return null;
		} catch (IOException e) {
			throw new IllegalArgumentException(e.toString());
		}
	}
 
	/**
	 * To byte array.
	 *
	 * @param bufferedImage the buffered image
	 * @return the byte[]
	 */
	private byte[] toByteArray(BufferedImage bufferedImage) {
		if (bufferedImage != null) {
			BufferedImage image = bufferedImage;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				if(bufferedImage.getTransparency() == Transparency.TRANSLUCENT)
					ImageIO.write(image, "png", baos);
				else
					ImageIO.write(image, "jpg", baos);
			} catch (IOException e) {
				throw new IllegalStateException(e.toString());
			}
			byte[] b = baos.toByteArray();
			return b;
		}
		return new byte[0];
	}
}
