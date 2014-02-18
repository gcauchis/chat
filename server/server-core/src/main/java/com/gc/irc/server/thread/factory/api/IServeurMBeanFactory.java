package com.gc.irc.server.thread.factory.api;

import com.gc.irc.common.ILoggable;
import com.gc.irc.server.thread.api.IServeurMBean;

/**
 * A factory for creating IServeurMBean objects.
 */
public interface IServeurMBeanFactory extends ILoggable {

    /**
     * Gets the serveur m bean.
     * 
     * @param parent
     *            the parent
     * @return the serveur m bean
     */
    IServeurMBean getServeurMBean();

}
