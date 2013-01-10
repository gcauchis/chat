package com.gc.irc.common.abs;

import org.slf4j.Logger;

import com.gc.irc.common.api.ILoggable;
import com.gc.irc.common.utils.LoggerUtils;

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
            log = LoggerUtils.getLogger(this.getClass());
        }
        return log;
    }

}
