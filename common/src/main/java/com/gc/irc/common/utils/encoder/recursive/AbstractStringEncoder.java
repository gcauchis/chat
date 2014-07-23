package com.gc.irc.common.utils.encoder.recursive;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.exception.utils.EncoderException;

/**
 * The Class AbstractStringEncoder.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public abstract class AbstractStringEncoder extends AbstractLoggable implements ObjectEncoder, StringEncoder {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.common.utils.encoder.IObjectEncoder#encodeClass(java.lang.
     * Class)
     */
    /** {@inheritDoc} */
    public boolean encodeClass(final Class<?> clazz) {
        return String.class.equals(clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.common.utils.encoder.IObjectEncoder#encodeObject(java.lang
     * .Object, com.gc.irc.common.utils.encoder.IStringEncoder)
     */
    /** {@inheritDoc} */
    public Object encodeObject(final Object value, final StringEncoder stringEncoder) throws EncoderException {
        if (value == null) {
            getLog().debug("null entry");
            return null;
        } else if (!encodeClass(value.getClass())) {
            getLog().warn("Not encodable class");
            throw new EncoderException("Not encodable class");
        } else if (stringEncoder != null && !equals(stringEncoder)) {
            return stringEncoder.encode((String) value);
        }
        return encode((String) value);
    }

}
