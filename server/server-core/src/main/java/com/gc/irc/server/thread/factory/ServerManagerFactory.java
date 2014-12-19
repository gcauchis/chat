package com.gc.irc.server.thread.factory;

import com.gc.irc.common.Loggable;
import com.gc.irc.server.thread.ServerManager;

/**
 * A factory for creating IServeurMBean objects.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface ServerManagerFactory extends Loggable {

    /**
     * Gets the serveur m bean.
     *
     * @return the serveur m bean
     */
    ServerManager getServeurManager();

}
