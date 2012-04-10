package com.gc.irc.common.utils.encoder;

import com.gc.irc.common.exception.utils.EncoderException;

/**
 * The Interface IObjectEncoder.
 */
public interface IObjectEncoder {

    /**
     * Check if the class is encodable by the current Encoder
     * 
     * @param clazz
     *            the clazz
     * @return true, if the given class is encodable by the Object Encoder
     */
    boolean encodeClass(Class<?> clazz);

    /**
     * Encode object.
     * 
     * @param value
     *            the value
     * @param stringEncoder
     *            the string encoder
     * @return the object
     * @throws EncoderException
     *             throh if an error occur
     */
    Object encodeObject(final Object value, final IStringEncoder stringEncoder) throws EncoderException;
}
