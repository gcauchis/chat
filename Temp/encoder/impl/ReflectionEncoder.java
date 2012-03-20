package com.acp.vision.encoder.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;

import javolution.util.FastList;
import javolution.util.FastSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acp.common.crypto.api.IObjectEncoder;
import com.acp.common.crypto.api.IStringEncoder;
import com.acp.common.crypto.exception.EncoderException;
import com.acp.vision.encoder.IReflectionEncoder;
import com.acp.vision.service.ILoggable;

/**
 * The Class ReflectionEncoder.
 */
public final class ReflectionEncoder implements ILoggable, IReflectionEncoder {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5755924805672846428L;

    /** The LOGGER of ReflectionEncoder. */
    private static final transient Logger LOGGER = LoggerFactory.getLogger(ReflectionEncoder.class);

    /** The Constant WRAPPER_TYPES. */
    @SuppressWarnings("unchecked")
    private static final Set < Class > WRAPPER_TYPES = new HashSet < Class >(Arrays.asList(Boolean.class, Character.class, Byte.class, Short.class, Integer.class,
            Long.class, Float.class, Double.class, Void.class, BigDecimal.class, BigInteger.class));

    /** The Constant NOT_ENCODABLE_TYPES. */
    @SuppressWarnings("unchecked")
    private static final Set < Class > NOT_ENCODABLE_TYPES = new HashSet < Class >(Arrays.asList(Object.class, String.class, java.util.Date.class, java.sql.Date.class,
            Calendar.class, Locale.class, TimeZone.class, GregorianCalendar.class));

    /* (non-Javadoc)
     * @see com.acp.vision.service.ILoggable#getLog()
     */
    @Override
    public Logger getLog() {
        return LOGGER;
    }

    /** The stringEncoder. */
    private IStringEncoder stringEncoder;

    /** The object encoders. */
    private List < IObjectEncoder > objectEncoders = new FastList < IObjectEncoder >();

    /** The encoded objects. */
    private Set < Object > encodedObjects = new FastSet < Object >();

    /** The encode all string. */
    private boolean encodeAllString = false;

    /** The class black list. */
    @SuppressWarnings("unchecked")
    private Set < Class > classBlackList = new FastSet < Class >();

    /**
     * Instantiates a new reflection stringEncoder.
     * 
     * @param encoder the encoder
     */
    public ReflectionEncoder(IStringEncoder encoder) {
        super();
        this.stringEncoder = encoder;
        if (encoder instanceof IObjectEncoder) {
            encodeAllString = true;
            objectEncoders.add((IObjectEncoder) encoder);
        }
    }

    /**
     * Instantiates a new reflection encoder.
     * 
     * @param stringEncoder the string encoder
     * @param objectEncoders the object encoders
     */
    public ReflectionEncoder(IStringEncoder stringEncoder, List < IObjectEncoder > objectEncoders) {
        this(stringEncoder);
        this.objectEncoders.addAll(objectEncoders);
    }

    /**
     * Instantiates a new reflection encoder.
     * 
     * @param stringEncoder the string encoder
     * @param blackList the black list
     */
    @SuppressWarnings("unchecked")
    public ReflectionEncoder(IStringEncoder stringEncoder, Collection < Class > blackList) {
        this(stringEncoder);
        addToBlackList(blackList);
    }

    /**
     * Instantiates a new reflection encoder.
     * 
     * @param stringEncoder the string encoder
     * @param objectEncoders the object encoders
     * @param blackList the black list
     */
    @SuppressWarnings("unchecked")
    public ReflectionEncoder(IStringEncoder stringEncoder, List < IObjectEncoder > objectEncoders, Collection < Class > blackList) {
        this(stringEncoder);
        this.objectEncoders.addAll(objectEncoders);
        addToBlackList(blackList);
    }

    /* (non-Javadoc)
     * @see com.acp.vision.encoder.IReflectionEncoder#addToBlackList(java.util.Collection)
     */
    @SuppressWarnings("unchecked")
    public final void addToBlackList(Collection < Class > list) {
        classBlackList.addAll(list);
    }

    /* (non-Javadoc)
     * @see com.acp.vision.encoder.IReflectionEncoder#encodeString(java.lang.String)
     */
    public final String encodeString(String value) {
        if (value != null) {
            return stringEncoder.encode(value);
        }
        return null;
    }

    /**
     * Checks if is encodable.
     * 
     * @param clazz
     *            the clazz
     * @return true, if is encodable
     */
    @SuppressWarnings("unchecked")
    private static boolean isEncodableField(Class clazz) {
        return !(WRAPPER_TYPES.contains(clazz) || clazz.isEnum());
    }

    /**
     * Checks if is not an encodable type.
     * 
     * @param clazz
     *            the clazz
     * @return true, if is not an encodable type
     */
    @SuppressWarnings("unchecked")
    private boolean isNotAnEncodableClass(Class clazz) {
        return NOT_ENCODABLE_TYPES.contains(clazz) || !isEncodableField(clazz) || classBlackList.contains(clazz);
    }

    /* (non-Javadoc)
     * @see com.acp.vision.encoder.IReflectionEncoder#encodeByReflection(java.lang.Object)
     */
    public final synchronized void encodeByReflection(final Object value) {
        reset();
        internalEncodeByReflection(value);
    }

    /**
     * Internal encode by reflection.
     * 
     * @param value the value
     */
    @SuppressWarnings("unchecked")
    private void internalEncodeByReflection(final Object value) {
        if (value != null) {
            Class clazz = value.getClass();
            if (isNotAnEncodableClass(clazz) || encodedObjects.contains(value)) {
                return;
            }
            encodedObjects.add(value);
            getLog().debug("Encode class ({}).", clazz.getName());
            if (value instanceof Collection < ? >) {
                encodeCollection((Collection < ? >) value);
            } else if (value instanceof Map < ?, ? >) {
                encodeMap((Map < ?, Object >) value);
            } else if (clazz.isArray()) {
                if (value instanceof Object[]) {
                    encodeArray((Object[]) value);
                }
            } else {
                IObjectEncoder objectEncoder = getEncoder(clazz);
                if (objectEncoder != null) {
                    try {
                        objectEncoder.encodeObject(value, stringEncoder);
                    } catch (EncoderException e) {
                        getLog().warn("Error while Encoding value.", e);
                    }
                }
                while (!clazz.equals(Object.class)) {
                    encodeClassFields(clazz, value);
                    clazz = clazz.getSuperclass();
                }
            }
        }
    }

    /**
     * Encode class fields.
     * 
     * @param clazz the clazz
     * @param value the value
     */
    @SuppressWarnings("unchecked")
    private void encodeClassFields(Class clazz, final Object value) {
        if (value != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String[] logParam = { field.getName(), field.getGenericType().toString() };
                getLog().debug("Encode field {} ({})", logParam);
                field.setAccessible(true);
                try {
                    if (String.class.equals(field.getGenericType()) && isAllStringEncoded()) {
                        field.set(value, encodeString((String) field.get(value)));
                    } else if (isEncodableField(clazz)) {
                        internalEncodeByReflection(field.get(value));
                    }
                } catch (IllegalAccessException e) {
                    getLog().debug("Innaccessible field {} ({})", logParam);
                }
            }
        }
    }

    /**
     * Gets the stringEncoder.
     * 
     * @param clazz the clazz
     * @return the stringEncoder
     */
    private IObjectEncoder getEncoder(Class < ? > clazz) {
        IObjectEncoder result = null;
        searchEncoder: for (IObjectEncoder objectEncoder : objectEncoders) {
            if (objectEncoder.encodeClass(clazz)) {
                result = objectEncoder;
                break searchEncoder;
            }
        }
        if (result != null && String.class.equals(clazz) && !encodeAllString) {
            encodeAllString = true;
        }
        return result;
    }

    /**
     * Encode array.
     * 
     * @param values the values
     */
    private void encodeArray(final Object[] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                if (String.class.equals(values[i].getClass()) && isAllStringEncoded()) {
                    values[i] = encodeString((String) values[i]);
                } else {
                    internalEncodeByReflection(values[i]);
                }
            }
        }
    }

    /**
     * Encode map.
     * 
     * @param value the value
     */
    private void encodeMap(final Map < ?, Object > value) {
        for (Entry < ?, Object > entry : value.entrySet()) {
            if (entry.getValue() != null) {
                if (String.class.equals(entry.getValue().getClass()) && isAllStringEncoded()) {
                    entry.setValue(encodeString((String) entry.getValue()));
                } else {
                    internalEncodeByReflection(entry.getValue());
                }
            }
        }
    }

    /**
     * Encode collection.
     * 
     * @param value the value
     */
    @SuppressWarnings("unchecked")
    private void encodeCollection(final Collection < ? > value) {
        List < Object > elementsToRemove = new FastList < Object >();
        List < Object > elementsModified = new FastList < Object >();
        for (Object obj : value) {
            if (obj != null) {
                if (String.class.equals(obj.getClass()) && isAllStringEncoded()) {
                    elementsToRemove.add(obj);
                    elementsModified.add(encodeString((String) obj));
                } else {
                    internalEncodeByReflection(obj);
                }
            }
        }
        if (elementsToRemove.size() > 0) {
            ((Collection < ? >) value).removeAll(elementsToRemove);
            ((Collection < Object >) value).addAll((Collection < Object >) elementsModified);
        }
    }

    /**
     * Checks if is all string encoded.
     * 
     * @return true, if is all string encoded
     */
    private boolean isAllStringEncoded() {
        return encodeAllString || getEncoder(String.class) != null;
    }

    /**
     * Reset.
     */
    private void reset() {
        encodedObjects = new FastSet < Object >();
    }
}