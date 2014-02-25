package com.gc.irc.server.thread.factory;

import com.gc.irc.common.ILoggable;
import com.gc.irc.server.thread.IServeurMBean;

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
