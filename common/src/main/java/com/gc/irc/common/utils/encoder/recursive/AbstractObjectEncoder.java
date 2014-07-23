package com.gc.irc.common.utils.encoder.recursive;

import java.lang.reflect.ParameterizedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.exception.utils.EncoderException;

/**
 * The Class AbstractObjectEncoder.
 *
 * @param <OBJ>
 *            the generic type
 * @author gcauchis
 * @version 0.0.4
 */
public abstract class AbstractObjectEncoder<OBJ> extends AbstractLoggable implements ObjectEncoder {

    /**
     * Gets the agg class.
     * 
     * @return the agg class
     */
    @SuppressWarnings("unchecked")
    private Class<OBJ> getGenericObjectClass() {
        final ParameterizedType parameterizedType = (ParameterizedType) retreiveDirectSubClass().getGenericSuperclass();
        return (Class<OBJ>) parameterizedType.getActualTypeArguments()[0];
    }

    /**
     * Retreive direct sub class.
     * 
     * @return the class
     */
    @SuppressWarnings("unchecked")
    private Class retreiveDirectSubClass() {
        Class clazz = getClass();
        while (clazz.getSuperclass() != AbstractObjectEncoder.class) {
            clazz = clazz.getSuperclass();
        }
        return clazz;
    }

    /** {@inheritDoc} */
    public final boolean encodeClass(final Class<?> clazz) {
        return getGenericObjectClass().equals(clazz);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public final Object encodeObject(final Object value, final StringEncoder stringEncoder) throws EncoderException {
        if (value == null) {
            getLog().debug("Null entry");
            return null;
        } else if (stringEncoder == null) {
            getLog().warn("Null stringEncoder");
            throw new IllegalArgumentException("Null stringEncoder");
        } else if (!encodeClass(value.getClass())) {
            getLog().warn("Not encodable class");
            throw new EncoderException("Not encodable class");
        }
        return internalEncodeObject((OBJ) value, stringEncoder);
    }

    /**
     * Internal encode object.
     *
     * @param obj
     *            the obj
     * @param stringEncoder
     *            the string encoder
     * @return the oBJ
     * @throws com.gc.irc.common.exception.utils.EncoderException
     *             the encoder exception
     */
    protected abstract OBJ internalEncodeObject(final OBJ obj, final StringEncoder stringEncoder) throws EncoderException;

}
