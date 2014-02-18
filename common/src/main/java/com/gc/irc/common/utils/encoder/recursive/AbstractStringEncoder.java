package com.gc.irc.common.utils.encoder.recursive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.irc.common.exception.utils.EncoderException;

/**
 * The Class AbstractStringEncoder.
 */
public abstract class AbstractStringEncoder implements IObjectEncoder, IStringEncoder {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(AbstractObjectEncoder.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.common.utils.encoder.IObjectEncoder#encodeClass(java.lang.
     * Class)
     */
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
    public Object encodeObject(final Object value, final IStringEncoder stringEncoder) throws EncoderException {
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

    public Logger getLog() {
        return LOGGER;
    }

}
