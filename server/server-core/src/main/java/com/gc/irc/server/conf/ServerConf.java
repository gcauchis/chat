package com.gc.irc.server.conf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.gc.irc.common.utils.LoggerUtils;

/**
 * An object use to configure the server.
 * 
 * @author gcauchis
 * 
 */
public class ServerConf {

    /** The Constant CONF_FILE_PATH. */
    private static final String CONF_FILE_PATH = "conf/servConf.properties";

    /** The Constant JMS_POOL_SIZE. */
    public static final String JMS_POOL_SIZE = "jmsPoolSize";

    /** The Constant JMS_SERVER_URL. */
    public static final String JMS_SERVER_URL = "jms.server.url";

    /** The Constant logger. */
    private static final Logger LOGGER = LoggerUtils.getLogger(ServerConf.class);

    /** The Constant NB_CONSUMER_THREAD. */
    public static final String NB_CONSUMER_THREAD = "nbConsumerThread";

    /** The Constant NB_MESSAGE_MAX_PASSAGE. */
    public static final String NB_MESSAGE_MAX_PASSAGE = "nbMessageMaxPassage";

    /** The properties. */
    private static Properties properties;

    /**
     * Get the property.
     * 
     * @param key
     *            Key of the property.
     * @param defaultValue
     *            Default value.
     * @return The Property value.
     */
    public static synchronized String getConfProperty(final String key, final String defaultValue) {
        return getProperty(key, defaultValue);
    }

    /**
     * Gets the properties.
     * 
     * @return the properties
     */
    public static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            LOGGER.info("load " + CONF_FILE_PATH);
            URL confUrl = ClassLoader.getSystemResource(CONF_FILE_PATH);
            if (confUrl != null) {
                try {
                    properties.load(confUrl.openStream());
                } catch (final FileNotFoundException e) {
                    LOGGER.warn("Configuration file not found.", e);
                } catch (final IOException e) {
                    LOGGER.warn("Failes to read configuration.", e);
                }
            }
        }
        return properties;
    }

    /**
     * Gets the property.
     * 
     * @param key
     *            the key
     * @return the property
     */
    public static String getProperty(final String key) {
        return getProperty(key, null);
    }

    /**
     * Gets the property.
     * 
     * @param key
     *            the key
     * @param defaultValue
     *            the default value
     * @return the property
     */
    public static String getProperty(final String key, final String defaultValue) {
        LOGGER.debug("Search " + key + (StringUtils.isBlank(defaultValue) ? "" : " (default value : " + defaultValue + ")"));
        final String result = getProperties().getProperty(key, defaultValue);
        return result == null ? null : result.trim();
    }

    /**
     * Instantiates a new properties utils.
     */
    private ServerConf() {
        super();
    }
}
