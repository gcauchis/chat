package com.acp.common.crypto.abs;

import com.acp.common.crypto.api.IObjectEncoder;
import com.acp.common.crypto.api.IStringEncoder;
import com.acp.common.crypto.exception.EncoderException;

/**
 * The Class AbstractStringEncoder.
 */
public abstract class AbstractStringEncoder implements IObjectEncoder, IStringEncoder {

    @Override
    public boolean encodeClass(Class < ? > clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public Object encodeObject(Object value, IStringEncoder stringEncoder) throws EncoderException {
        if (value == null) {
            return null;
        } else if (!encodeClass(value.getClass())) {
            throw new EncoderException("Not encodable class");
        } else if (stringEncoder != null && !this.equals(stringEncoder)) {
            return stringEncoder.encode((String) value);
        }
        return encode((String) value);
    }

}
