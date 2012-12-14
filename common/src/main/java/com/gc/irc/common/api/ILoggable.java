package com.gc.irc.common.api;

import java.io.Serializable;

import org.slf4j.Logger;

/**
 * The Interface ILoggable.
 */
public interface ILoggable extends Serializable {

    /**
     * Gets the log.
     * 
     * @return the log
     */
    Logger getLog();
}
