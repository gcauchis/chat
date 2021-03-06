package com.gc.irc.common.utils.encoder.recursive;

import java.util.Collection;

import com.gc.irc.common.Loggable;

/**
 * <p>IReflectionEncoder interface.</p>
 *
 * @version 0.0.4
 * @author x472511
 */
public interface ReflectionEncoder extends Loggable {

    /**
     * Add a list of class who cannot be encoded. If a class provoke a stack
     * overflow put it in this list. A stack overflow may occur because of
     * circular field dependency.
     *
     * @param list
     *            the list
     */
    @SuppressWarnings("unchecked")
    void addToBlackList(Collection<Class> list);

    /**
     * Adds the interfaces to black list.
     *
     * @param list
     *            the list
     */
    @SuppressWarnings("unchecked")
    void addInterfacesToBlackList(Collection<Class> list);

    /**
     * Encode the given string using the given stringEncoder.
     *
     * @param value
     *            the value
     * @return the string
     */
    String encodeString(String value);

    /**
     * Encode by reflection all the string in the given object. WARNING encoding
     * can throw a stack overflow if you want to encode an abject with circular
     * dependency. Use addToBlackList to prevent that.
     *
     * @param value
     *            the value
     */
    void encodeByReflection(final Object value);

    /**
     * Sets the max deep.
     *
     * @param maxDeep
     *            the new max deep
     */
    void setMaxDeep(int maxDeep);

}
