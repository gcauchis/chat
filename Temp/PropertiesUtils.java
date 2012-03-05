package com.acp.vision.security.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class PropertiesUtils.
 */
public final class PropertiesUtils {

    /** The LOGGER of PropertiesUtils. */
    private static final transient Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    /** The Constant CONF_FILE_PATH. */
    private static final String CONF_FILE_PATH = "conf/conf.properties";

    /** The properties. */
    private static Properties properties;

    /**
     * Instantiates a new properties utils.
     */
    private PropertiesUtils() {
        super();
    }

    /**
     * Gets the properties.
     * 
     * @return the properties
     */
    private static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            LOGGER.info("load {}", CONF_FILE_PATH);
            try {
                properties.load(ClassLoader.getSystemResource(CONF_FILE_PATH).openStream());
            } catch (FileNotFoundException e) {
                LOGGER.warn("Configuration file not found.", e);
            } catch (IOException e) {
                LOGGER.warn("Failes to read configuration.", e);
            }
        }
        return properties;
    }

    /**
     * Gets the property.
     * 
     * @param key the key
     * @return the property
     */
    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    /**
     * Gets the property.
     * 
     * @param key the key
     * @param defaultValue the default value
     * @return the property
     */
    public static String getProperty(String key, String defaultValue) {
        LOGGER.debug("Search {} (default value : {})", key, defaultValue);
        return getProperties().getProperty(key, defaultValue);
    }

}
