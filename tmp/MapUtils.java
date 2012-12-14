/*
 * 
 */
package com.acp.acs.common.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The Class MapUtils.
 * 
 * @author g.migliorini
 */
public final class MapUtils {

    /**
     * Instantiates a new map utils.
     */
    private MapUtils() {
        super();
    }

    /**
     * Gets the parameter.
     * 
     * @param params
     *            the params
     * @param param
     *            the param
     * @return the parameter
     */
    public static Serializable getParameter(final Map < String, Serializable > _params, final String _param) {
        for (final Entry < String, Serializable > currentParam : _params.entrySet()) {
            if (currentParam.getKey().equals(_param)) {
                return currentParam.getValue();
            }
        }
        return null;
    }

    /**
     * Gets the string parameter.
     * 
     * @param params
     *            the params
     * @param param
     *            the param
     * @return the string parameter
     */
    public static String getStringParameter(final Map < String, Serializable > _params, final String _param) {
        return (String) getParameter(_params, _param);
    }

    /**
     * Gets the integer parameter.
     * 
     * @param params
     *            the params
     * @param param
     *            the param
     * @return the integer parameter
     */
    public static Integer getIntegerParameter(final Map < String, Serializable > _params, final String _param) {
        return (Integer) getParameter(_params, _param);
    }

    /**
     * Gets the date parameter.
     * 
     * @param params
     *            the params
     * @param param
     *            the param
     * @return the date parameter
     */
    public static Date getDateParameter(final Map < String, Serializable > _params, final String _param) {
        return (Date) getParameter(_params, _param);
    }

    /**
     * The Class Builder.
     * 
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     */
    public static class Builder < K, V > {

        /** The map. */
        private final Map < K, V > map;

        /**
         * Instantiates a new builder.
         * 
         * @param map
         *            the map
         */
        private Builder(final Map < K, V > map) {
            super();
            this.map = map;
        }

        /**
         * New instance.
         * 
         * @param <K>
         *            the key type
         * @param <V>
         *            the value type
         * @param map
         *            the map
         * @return the builder
         */
        public static < K, V > Builder < K, V > newInstance(final Map < K, V > map) {
            return new Builder <>(map);
        }

        /**
         * Put.
         * 
         * @param key
         *            the key
         * @param value
         *            the value
         * @return the builder
         */
        public Builder < K, V > put(final K key, final V value) {
            map.put(key, value);
            return this;
        }

        /**
         * Builds the.
         * 
         * @return the map
         */
        public Map < K, V > build() {
            return map;
        }

    }

}
