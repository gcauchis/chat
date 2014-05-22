package com.gc.irc.server.client.connector;

import com.gc.irc.common.AbstractLoggable;

/**
 * The Class AbstractClientConnector.
 */
public abstract class AbstractClientConnector extends AbstractLoggable
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
