package com.gc.irc.server.thread.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.server.core.user.management.api.IUserConnectionsManagement;
import com.gc.irc.server.thread.api.IServeurMBean;
import com.gc.irc.server.thread.factory.api.IServeurMBeanFactory;
import com.gc.irc.server.thread.impl.ServeurMBean;

/**
 * A factory for creating ServeurMBean objects.
 */
@Component("ServeurMBeanFactory")
@Scope("singleton")
public class ServeurMBeanFactory extends AbstractLoggable implements IServeurMBeanFactory {

    /** The user connections management. */
    @Autowired
    private IUserConnectionsManagement userConnectionsManagement;

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.factory.api.IServeurMBeanFactory#getServeurMBean(com.gc.irc.server.core.ServerCore)
     */
    @Override
    public IServeurMBean getServeurMBean() {
        return new ServeurMBean(userConnectionsManagement);
    }

    public void setUserConnectionsManagement(IUserConnectionsManagement userConnectionsManagement) {
        this.userConnectionsManagement = userConnectionsManagement;
    }

}
