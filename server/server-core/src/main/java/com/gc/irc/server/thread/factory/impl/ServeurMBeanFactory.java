package com.gc.irc.server.thread.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.jms.api.IJMSProducer;
import com.gc.irc.server.thread.api.IServeurMBean;
import com.gc.irc.server.thread.factory.api.IServeurMBeanFactory;
import com.gc.irc.server.thread.impl.ServeurMBean;

/**
 * A factory for creating ServeurMBean objects.
 */
@Component("ServeurMBeanFactory")
@Scope("singleton")
public class ServeurMBeanFactory extends AbstractLoggable implements IServeurMBeanFactory {

    /** The jms producer. */
    @Autowired
    private IJMSProducer jmsProducer;

    /** The num passage max. */
    @Value("${nbMessageMaxPassage}")
    private int numPassageMax = 10;

    /** The users connections management. */
    @Autowired
    private IUsersConnectionsManagement usersConnectionsManagement;

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.factory.api.IServeurMBeanFactory#getServeurMBean(com.gc.irc.server.core.ServerCore)
     */
    @Override
    public IServeurMBean getServeurMBean() {
        ServeurMBean serveurMBean = new ServeurMBean();
        serveurMBean.setUsersConnectionsManagement(usersConnectionsManagement);
        serveurMBean.setJmsProducer(jmsProducer);
        serveurMBean.setNumPassageMax(numPassageMax);
        return serveurMBean;
    }

    /**
     * Sets the jms producer.
     * 
     * @param jmsProducer
     *            the new jms producer
     */
    public void setJmsProducer(IJMSProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }

    /**
     * Sets the num passage max.
     * 
     * @param numPassageMax
     *            the new num passage max
     */
    public void setNumPassageMax(int numPassageMax) {
        this.numPassageMax = numPassageMax;
    }

    /**
     * Sets the users connections management.
     * 
     * @param usersConnectionsManagement
     *            the new users connections management
     */
    public void setUsersConnectionsManagement(IUsersConnectionsManagement usersConnectionsManagement) {
        this.usersConnectionsManagement = usersConnectionsManagement;
    }

}
