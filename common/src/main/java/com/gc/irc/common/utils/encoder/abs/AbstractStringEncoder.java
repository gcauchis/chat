package com.gc.irc.common.utils.encoder.abs;

import com.gc.irc.common.exception.utils.EncoderException;
import com.gc.irc.common.utils.encoder.IObjectEncoder;
import com.gc.irc.common.utils.encoder.IStringEncoder;

/**
 * The Class AbstractStringEncoder.
 */
public abstract class AbstractStringEncoder implements IObjectEncoder, IStringEncoder {

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
            return null;
        } else if (!encodeClass(value.getClass())) {
            throw new EncoderException("Not encodable class");
        } else if (stringEncoder != null && !equals(stringEncoder)) {
            return stringEncoder.encode((String) value);
        }
        return encode((String) value);
    }

}
