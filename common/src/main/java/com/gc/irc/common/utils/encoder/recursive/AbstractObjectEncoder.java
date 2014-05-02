package com.gc.irc.common.utils.encoder.recursive;

import java.lang.reflect.ParameterizedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.irc.common.exception.utils.EncoderException;

/**
 * The Class AbstractObjectEncoder.
 *
 * @param <OBJ>
 *            the generic type
 * @author gcauchis
 * @version 0.0.4
 */
public abstract class AbstractObjectEncoder<OBJ> implements IObjectEncoder {

    /** The Constant LOGGER. */
    private static final transient Logger LOGGER = LoggerFactory.getLogger(AbstractObjectEncoder.class);

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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.acp.common.crypto.api.IObjectEncoder#encryptClass(java.lang.Class)
     */
    /** {@inheritDoc} */
    public final boolean encodeClass(final Class<?> clazz) {
        return getGenericObjectClass().equals(clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.acp.common.crypto.api.IObjectEncoder#encryptObject(java.lang.Object,
     * com.acp.common.crypto.api.IStringEncoder)
     */
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public final Object encodeObject(final Object value, final IStringEncoder stringEncoder) throws EncoderException {
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
    protected abstract OBJ internalEncodeObject(final OBJ obj, final IStringEncoder stringEncoder) throws EncoderException;

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.common.api.ILoggable#getLog()
     */
    /**
     * <p>getLog.</p>
     *
     * @return a {@link org.slf4j.Logger} object.
     */
    public Logger getLog() {
        return LOGGER;
    }

}
