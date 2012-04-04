package com.acp.vision.encoder;

import java.lang.reflect.ParameterizedType;

import com.acp.common.crypto.api.IObjectEncoder;
import com.acp.common.crypto.api.IStringEncoder;
import com.acp.common.crypto.exception.EncoderException;

/**
 * The Class AbstractObjectEncoder.
 * 
 * @param <OBJ> the generic type
 */
public abstract class AbstractObjectEncoder < OBJ > implements IObjectEncoder {

    /**
     * Gets the obj class.
     * 
     * @return the obj class
     */
    @SuppressWarnings("unchecked")
    private Class < OBJ > getGenericObjectClass() {
        final ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class < OBJ >) parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public final boolean encodeClass(Class < ? > clazz) {
        return getGenericObjectClass().equals(clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Object encodeObject(final Object value, final IStringEncoder stringEncoder) throws EncoderException {
        if (value == null) {
            return null;
        } else if (stringEncoder == null) {
            throw new IllegalArgumentException("Null stringEncoder");
        } else if (!encodeClass(value.getClass())) {
            throw new EncoderException("Not encodable class");
        }
        return internalEncodeObject((OBJ) value, stringEncoder);
    }

    /**
     * Internal encode object.
     * 
     * @param obj the obj
     * @param stringEncoder the string encoder
     * @return the oBJ
     */
    protected abstract OBJ internalEncodeObject(final OBJ obj, final IStringEncoder stringEncoder);

}
