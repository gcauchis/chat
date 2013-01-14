package com.gc.irc.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.server.core.ServerCore;

/**
 * The Class ServerStarter.
 */
public class ServerStarter extends AbstractLoggable implements Runnable {

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {
        final ServerStarter starter = new ServerStarter();
        starter.startAndWaitForClient();
    }

    /** The initialized. */
    private boolean initialized = false;

    /**
     * Checks if is initialized.
     * 
     * @return true, if is initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        startAndWaitForClient();
    }

    /**
     * Start and wait for client.
     */
    public void startAndWaitForClient() {
        getLog().info("Load context");
        final ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring-application-config.xml");
        getLog().info("Retreive server core bean");
        final ServerCore core = (ServerCore) context.getBean("serverCore");
        getLog().info("Init server");
        core.initServeur();
        initialized = true;
        getLog().info("Start Waiting for client");
        while (true) {
            core.waitClient();
        }
    }

}
