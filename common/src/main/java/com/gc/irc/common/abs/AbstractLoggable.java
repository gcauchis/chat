package com.gc.irc.common.abs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.irc.common.api.ILoggable;

/**
 * The Class AbstractLoggable.
 */
public abstract class AbstractLoggable implements ILoggable {

    /** The log. */
    private Logger log = null;

    /**
     * {@inheritDoc}
     * 
     * @see com.acp.acs.common.api.vision.service.ILoggable#getLog()
     */
    @Override
    public Logger getLog() {
        if (log == null) {
            log = LoggerFactory.getLogger(this.getClass());
        }
        return log;
    }

}
