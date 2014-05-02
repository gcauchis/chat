package com.gc.irc.server.thread.factory;

import com.gc.irc.common.ILoggable;
import com.gc.irc.server.thread.IServeurMBean;

/**
 * A factory for creating IServeurMBean objects.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface IServeurMBeanFactory extends ILoggable {

    /**
     * Gets the serveur m bean.
     *
     * @return the serveur m bean
     */
    IServeurMBean getServeurMBean();

}
