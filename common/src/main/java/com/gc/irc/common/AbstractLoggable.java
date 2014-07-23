package com.gc.irc.common;

import org.slf4j.Logger;

import com.gc.irc.common.utils.LoggerUtils;

/**
 * The Class AbstractLoggable.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public abstract class AbstractLoggable implements Loggable {

    /** The log. */
    private Logger log = null;

    @Override
    public Logger getLog() {
        if (log == null) {
            log = LoggerUtils.getLogger(this.getClass());
        }
        return log;
    }

}
