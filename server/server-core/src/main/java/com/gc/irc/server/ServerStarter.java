package com.gc.irc.server;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.server.conf.ServerConf;
import com.gc.irc.server.core.ServerCore;

/**
 * The Class ServerStarter.
 */
public class ServerStarter extends AbstractLoggable implements Runnable {

    /** The initialized. */
    private boolean initialized = false;

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

    /**
     * Start and wait for client.
     */
    public void startAndWaitForClient() {
        final XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("spring-application-config.xml"));
        final PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
        cfg.setProperties(ServerConf.getProperties());
        cfg.postProcessBeanFactory(beanFactory);
        final ServerCore core = (ServerCore) beanFactory.getBean("serverCore");
        getLog().info("Init server");
        core.initServeur();
        initialized = true;
        getLog().info("Start Waiting for client");
        while (true) {
            core.waitClient();
        }
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
     * Checks if is initialized.
     * 
     * @return true, if is initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

}
