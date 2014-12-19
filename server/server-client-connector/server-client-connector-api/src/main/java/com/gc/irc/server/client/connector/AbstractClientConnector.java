package com.gc.irc.server.client.connector;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.AbstractRunnable;

/**
 * The Class AbstractClientConnector.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public abstract class AbstractClientConnector extends AbstractRunnable
		implements ClientConnector {
	
	/** The initialized. */
    private boolean initialized = false;
    
    /**
     * Checks if is initialized.
     *
     * @return a boolean.
     */
    public boolean isInitialized() {
        return initialized;
    }

	/**
	 * Run.
	 */
	public void run() {
		getLog().info("Initialize connector.");
		initConnector();
		initialized = true;
		getLog().info("Connector initialized.");
		
		while (true)
		{
			getLog().debug("Wait for a new client.");
			waitClient();
		}

	}

}
