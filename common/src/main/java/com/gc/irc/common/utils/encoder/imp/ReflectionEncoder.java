package com.gc.irc.common.utils.encoder.imp;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Semaphore;

import javolution.util.FastList;
import javolution.util.FastSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.irc.common.exception.utils.EncoderException;
import com.gc.irc.common.utils.encoder.IObjectEncoder;
import com.gc.irc.common.utils.encoder.IReflectionEncoder;
import com.gc.irc.common.utils.encoder.IStringEncoder;

/**
 * The Class ReflectionEncoder.
 */
public final class ReflectionEncoder implements IReflectionEncoder {

    /** The LOGGER of ReflectionEncoder. */
    private static final transient Logger LOGGER = LoggerFactory.getLogger(ReflectionEncoder.class);

    /** The Constant DEFAULT_MAX_DEEP. */
    public static final int DEFAULT_MAX_DEEP = 40;

    /** The Constant WRAPPER_TYPES. */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static final Set<Class> WRAPPER_TYPES = new HashSet<Class>(Arrays.asList(Boolean.class, Character.class, Byte.class, Short.class, Integer.class,
            Long.class, Float.class, Double.class, Void.class, BigDecimal.class, BigInteger.class));

    /** The Constant NOT_ENCODABLE_TYPES. */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static final Set<Class> NOT_ENCODABLE_TYPES = new HashSet<Class>(Arrays.asList(Object.class, String.class, java.util.Date.class,
            java.sql.Date.class, Calendar.class, Locale.class, TimeZone.class, org.apache.log4j.Logger.class, AccessibleObject.class));

    /** The Constant NOT_ENCODABLE_INTERFACES. */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static final Set<Class> NOT_ENCODABLE_INTERFACES = new HashSet<Class>(Arrays.asList(Logger.class));

    /*
     * (non-Javadoc)
     * 
     * @see com.acp.vision.service.ILoggable#getLog()
     */
    public Logger getLog() {
        return LOGGER;
    }

    /** The stringEncoder. */
    private IStringEncoder stringEncoder;

    /** The object encoders. */
    private List<IObjectEncoder> objectEncoders = new FastList<IObjectEncoder>();

    /** The encoded objects. */
    private Set<Object> encodedObjects = new FastSet<Object>();

    /** The encoded string. */
    private Set<String> encodedString = new FastSet<String>();

    /** The encode all string. */
    private boolean encodeAllString = false;

    /** The class black list. */
    @SuppressWarnings("rawtypes")
    private Set<Class> classBlackList = new FastSet<Class>();

    /** The interfaces black list. */
    @SuppressWarnings("rawtypes")
    private Set<Class> interfacesBlackList = new FastSet<Class>();

    /** The lock. */
    private Semaphore lock = new Semaphore(1);

    /** The max deep. */
    private int maxDeep = DEFAULT_MAX_DEEP;

    /** The deep. */
    private int deep = 0;

    /**
     * Instantiates a new reflection stringEncoder.
     * 
     * @param encoder
     *            the encoder
     */
    public ReflectionEncoder(final IStringEncoder encoder) {
        super();
        stringEncoder = encoder;
        if (encoder instanceof IObjectEncoder) {
            encodeAllString = true;
            objectEncoders.add((IObjectEncoder) encoder);
        }
    }

    /**
     * Instantiates a new reflection encoder.
     * 
     * @param stringEncoder
     *            the string encoder
     * @param objectEncoders
     *            the object encoders
     */
    public ReflectionEncoder(final IStringEncoder stringEncoder, final List<IObjectEncoder> objectEncoders) {
        this(stringEncoder);
        this.objectEncoders.addAll(objectEncoders);
    }

    /**
     * Instantiates a new reflection encoder.
     * 
     * @param stringEncoder
     *            the string encoder
     * @param objectEncoders
     *            the object encoders
     * @param blackList
     *            the black list
     */
    public ReflectionEncoder(final IStringEncoder stringEncoder, final List<IObjectEncoder> objectEncoders,
            @SuppressWarnings("rawtypes") final Collection<Class> blackList) {
        this(stringEncoder, objectEncoders);
        addToBlackList(blackList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.acp.vision.encoder.IReflectionEncoder#addToBlackList(java.util.Collection
     * )
     */
    public final void addToBlackList(@SuppressWarnings("rawtypes") final Collection<Class> list) {
        classBlackList.addAll(list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.acp.vision.encoder.IReflectionEncoder#addInterfacesToBlackList(java
     * .util.Collection)
     */
    public final void addInterfacesToBlackList(@SuppressWarnings("rawtypes") final Collection<Class> list) {
        interfacesBlackList.addAll(list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.acp.vision.encoder.IReflectionEncoder#encodeString(java.lang.String)
     */
    public final String encodeString(final String value) {
        if (value != null) {
            if (encodedString.contains(value)) {
                return value;
            }
            final String result = stringEncoder.encode(value);
            encodedString.add(result);
            return result;
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
    private static boolean isEncodableField(@SuppressWarnings("rawtypes") final Class clazz) {
        return !(WRAPPER_TYPES.contains(clazz) || clazz.isEnum());
    }

    /**
     * Checks if is not an encodable type.
     * 
     * @param clazz
     *            the clazz
     * @return true, if is not an encodable type
     */
    private boolean isNotAnEncodableClass(@SuppressWarnings("rawtypes") final Class clazz) {
        return isInClassCollection(clazz, NOT_ENCODABLE_TYPES) || !isEncodableField(clazz) || isInClassCollection(clazz, classBlackList)
                || isInClassCollection(clazz.getInterfaces(), NOT_ENCODABLE_INTERFACES) || isInClassCollection(clazz.getInterfaces(), interfacesBlackList);
    }

    /**
     * Checks if is in class collection.
     * 
     * @param clazz
     *            the clazz
     * @param classes
     *            the classes
     * @return true, if is in class collection
     */
    @SuppressWarnings("rawtypes")
    private boolean isInClassCollection(Class clazz, final Collection<Class> classes) {
        while (clazz != null && !clazz.equals(Object.class)) {
            if (classes.contains(clazz)) {
                return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    /**
     * Checks if is in class collection.
     * 
     * @param classesTab
     *            the classes tab
     * @param classes
     *            the classes
     * @return true, if is in class collection
     */
    @SuppressWarnings("rawtypes")
    private boolean isInClassCollection(final Class[] classesTab, final Collection<Class> classes) {
        for (final Class clazz : classesTab) {
            if (clazz != null && isInClassCollection(clazz, classes)) {
                return true;
            }
        }
        return false;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.acp.vision.encoder.IReflectionEncoder#encodeByReflection(java.lang
     * .Object)
     */
    public final synchronized void encodeByReflection(final Object value) {
        try {
            lock.acquire();
        } catch (final InterruptedException e) {
            getLog().error("fail to acquire lock");
            return;
        }
        reset();
        internalEncodeByReflection(value);
        lock.release();
    }

    /**
     * Internal encode by reflection.
     * 
     * @param value
     *            the value
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void internalEncodeByReflection(final Object value) {
        deep++;
        if (deep > maxDeep) {
            deep--;
            return;
        }
        if (value != null) {
            final Class clazz = value.getClass();
            if (isNotAnEncodableClass(clazz) || encodedObjects.contains(value)) {
                deep--;
                return;
            }
            encodedObjects.add(value);
            getLog().debug("Encode class ({}).", clazz.getName());
            if (value instanceof Collection<?>) {
                encodeCollection((Collection<?>) value);
            } else if (value instanceof Map<?, ?>) {
                encodeMap((Map<?, Object>) value);
            } else if (clazz.isArray() && value instanceof Object[]) {
                encodeArray((Object[]) value);
            } else {
                final IObjectEncoder objectEncoder = getEncoder(clazz);
                if (objectEncoder != null) {
                    try {
                        objectEncoder.encodeObject(value, stringEncoder);
                    } catch (final EncoderException e) {
                        getLog().warn("Error while Encoding value.", e);
                    }
                }
                encodeClassFields(value);
            }
        }
        deep--;
    }

    /**
     * Encode class fields.
     * 
     * @param clazz
     *            the clazz
     * @param value
     *            the value
     */
    @SuppressWarnings("rawtypes")
    private void encodeClassFields(final Object value) {
        if (value != null) {
            Class clazz = value.getClass();
            while (clazz != null && !clazz.equals(Object.class)) {
                final Field[] fields = clazz.getDeclaredFields();
                for (final Field field : fields) {
                    final String[] logParam = { field.getName(), field.getGenericType().toString() };
                    getLog().debug("Encode field {} ({})", logParam);
                    field.setAccessible(true);
                    try {
                        if (String.class.equals(field.getGenericType()) && isAllStringEncoded()) {
                            field.set(value, encodeString((String) field.get(value)));
                        } else if (isEncodableField(clazz)) {
                            internalEncodeByReflection(field.get(value));
                        }
                    } catch (final IllegalAccessException e) {
                        getLog().debug("Innaccessible field {} ({})", logParam);
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }
    }

    /**
     * Gets the stringEncoder.
     * 
     * @param clazz
     *            the clazz
     * @return the stringEncoder
     */
    private IObjectEncoder getEncoder(final Class<?> clazz) {
        IObjectEncoder result = null;
        searchEncoder: for (final IObjectEncoder objectEncoder : objectEncoders) {
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
     * @param values
     *            the values
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
     * @param value
     *            the value
     */
    private void encodeMap(final Map<?, Object> value) {
        for (final Entry<?, Object> entry : value.entrySet()) {
            if (entry.getValue() != null) {
                getLog().debug("Encode map enty {}", entry.getKey());
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
     * @param value
     *            the value
     */
    @SuppressWarnings("unchecked")
    private void encodeCollection(final Collection<?> value) {
        final List<Object> elementsToRemove = new FastList<Object>();
        final List<Object> elementsModified = new FastList<Object>();
        for (final Object obj : value) {
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
            ((Collection<?>) value).removeAll(elementsToRemove);
            ((Collection<Object>) value).addAll(elementsModified);
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
        encodedString.clear();
        encodedObjects.clear();
        deep = 0;
    }

    /**
     * Gets the max deep.
     * 
     * @return the max deep
     */
    public int getMaxDeep() {
        return maxDeep;
    }

    /**
     * Sets the max deep. Must be > 5.
     * 
     * @param maxDeep
     *            the new max deep
     */
    public void setMaxDeep(final int maxDeep) {
        this.maxDeep = maxDeep > 5 ? maxDeep : DEFAULT_MAX_DEEP;
    }

}
