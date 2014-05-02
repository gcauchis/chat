package com.gc.irc.common.utils.encoder.recursive;

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

import com.gc.irc.common.exception.utils.EncoderException;
import com.gc.irc.common.utils.LoggerUtils;

/**
 * The Class ReflectionEncoder.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public final class ReflectionEncoder implements IReflectionEncoder {

    /** The Constant DEFAULT_MAX_DEEP. */
    public static final int DEFAULT_MAX_DEEP = 40;

    /** The LOGGER of ReflectionEncoder. */
    private static final transient Logger LOGGER = LoggerUtils.getLogger(ReflectionEncoder.class);

    /** The Constant NOT_ENCODABLE_INTERFACES. */
    @SuppressWarnings({ "unchecked" })
    private static final Set<Class> NOT_ENCODABLE_INTERFACES = new HashSet<Class>(Arrays.asList(Logger.class));

    /** The Constant NOT_ENCODABLE_TYPES. */
    @SuppressWarnings({ "unchecked" })
    private static final Set<Class> NOT_ENCODABLE_TYPES = new HashSet<Class>(Arrays.asList(Object.class, String.class, java.util.Date.class,
            java.sql.Date.class, Calendar.class, Locale.class, TimeZone.class, Logger.class, AccessibleObject.class));

    /** The Constant WRAPPER_TYPES. */
    @SuppressWarnings({ "unchecked" })
    private static final Set<Class> WRAPPER_TYPES = new HashSet<Class>(Arrays.asList(Boolean.class, Character.class, Byte.class, Short.class, Integer.class,
            Long.class, Float.class, Double.class, Void.class, BigDecimal.class, BigInteger.class));

    /**
     * Checks if is encodable.
     * 
     * @param clazz
     *            the clazz
     * @return true, if is encodable
     */
    @SuppressWarnings("unchecked")
    private static boolean isEncodableField(final Class clazz) {
        return !(WRAPPER_TYPES.contains(clazz) || clazz.isEnum());
    }

    /** The class black list. */
    @SuppressWarnings("unchecked")
    private Set<Class> classBlackList = new FastSet<Class>();

    /** The deep. */
    private int deep = 0;

    /** The encode all string. */
    private boolean encodeAllString = false;

    /** The encoded objects. */
    private Set<Object> encodedObjects = new FastSet<Object>();

    /** The encoded string. */
    private Set<String> encodedString = new FastSet<String>();

    /** The interfaces black list. */
    @SuppressWarnings("unchecked")
    private Set<Class> interfacesBlackList = new FastSet<Class>();

    /** The lock. */
    private Semaphore lock = new Semaphore(1);

    /** The max deep. */
    private int maxDeep = DEFAULT_MAX_DEEP;

    /** The object encoders. */
    private List<IObjectEncoder> objectEncoders = new FastList<IObjectEncoder>();

    /** The stringEncoder. */
    private IStringEncoder stringEncoder;

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
    @SuppressWarnings("unchecked")
    public ReflectionEncoder(final IStringEncoder stringEncoder, final List<IObjectEncoder> objectEncoders, final Collection<Class> blackList) {
        this(stringEncoder, objectEncoders);
        addToBlackList(blackList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.common.encoder.IReflectionEncoder#addInterfacesToBlackList(java
     * .util.Collection)
     */
    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public void addInterfacesToBlackList(final Collection<Class> list) {
        interfacesBlackList.addAll(list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.common.encoder.IReflectionEncoder#addToBlackList(java.util.Collection
     * )
     */
    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public void addToBlackList(final Collection<Class> list) {
        classBlackList.addAll(list);
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.common.encoder.IReflectionEncoder#encodeByReflection(java.lang
     * .Object)
     */
    /** {@inheritDoc} */
    @Override
    public synchronized void encodeByReflection(final Object value) {
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
     * Encode class fields.
     * 
     * @param clazz
     *            the clazz
     * @param value
     *            the value
     */
    @SuppressWarnings("unchecked")
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.common.encoder.IReflectionEncoder#encodeString(java.lang.String)
     */
    /** {@inheritDoc} */
    @Override
    public String encodeString(final String value) {
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

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.common.api.ILoggable#getLog()
     */
    /** {@inheritDoc} */
    @Override
    public Logger getLog() {
        return LOGGER;
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
     * Internal encode by reflection.
     * 
     * @param value
     *            the value
     */
    @SuppressWarnings({ "unchecked" })
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
     * Checks if is all string encoded.
     * 
     * @return true, if is all string encoded
     */
    private boolean isAllStringEncoded() {
        return encodeAllString || getEncoder(String.class) != null;
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
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
    private boolean isInClassCollection(final Class[] classesTab, final Collection<Class> classes) {
        for (final Class clazz : classesTab) {
            if (clazz != null && isInClassCollection(clazz, classes)) {
                return true;
            }
        }
        return false;

    }

    /**
     * Checks if is not an encodable type.
     * 
     * @param clazz
     *            the clazz
     * @return true, if is not an encodable type
     */
    @SuppressWarnings("unchecked")
    private boolean isNotAnEncodableClass(final Class clazz) {
        return isInClassCollection(clazz, NOT_ENCODABLE_TYPES) || !isEncodableField(clazz) || isInClassCollection(clazz, classBlackList)
                || isInClassCollection(clazz.getInterfaces(), NOT_ENCODABLE_INTERFACES) || isInClassCollection(clazz.getInterfaces(), interfacesBlackList);
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
     * {@inheritDoc}
     *
     * Sets the max deep. Must be > 5.
     */
    @Override
    public void setMaxDeep(final int maxDeep) {
        this.maxDeep = maxDeep > 5 ? maxDeep : DEFAULT_MAX_DEEP;
    }

}
