package com.gc.irc.server.thread.factory;

import com.gc.irc.common.Loggable;
import com.gc.irc.server.thread.ServerManager;

/**
 * A factory for creating IServeurMBean objects.
 *
 * @version 0.0.4
 * @author x472511
 */
public interface ServerManagerFactory extends Loggable {

    /**
     * Gets the serveur m bean.
     *
     * @return the serveur m bean
     */
    ServerManager getServeurManager();

}
