package com.gc.irc.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class LoggerUtils.
 *
 * @version 0.0.4
 * @author x472511
 */
public final class LoggerUtils {

    /**
     * Instantiates a new logger utils.
     */
    private LoggerUtils() {
    }

    /**
     * Gets the logger.
     *
     * @param clazz
     *            the clazz
     * @return the logger
     */
    @SuppressWarnings("rawtypes")
    public static Logger getLogger(final Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * Gets the logger.
     *
     * @param logger
     *            the logger
     * @return the logger
     */
    public static Logger getLogger(final String logger) {
        return LoggerFactory.getLogger(logger);
    }

}
