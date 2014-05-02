package com.gc.irc.common.utils.encoder.recursive;

import com.gc.irc.common.ILoggable;
import com.gc.irc.common.exception.utils.EncoderException;

/**
 * The Interface IObjectEncoder.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface IObjectEncoder extends ILoggable {

    /**
     * Check if the class is encodable by the current Encoder
     *
     * @param clazz
     *            the clazz
     * @return a boolean.
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
     * @throws com.gc.irc.common.exception.utils.EncoderException
     *             throh if an error occur
     */
    Object encodeObject(final Object value, final IStringEncoder stringEncoder) throws EncoderException;
}
