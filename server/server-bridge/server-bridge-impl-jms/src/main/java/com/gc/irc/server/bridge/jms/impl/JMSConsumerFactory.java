package com.gc.irc.server.bridge.jms.impl;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.server.bridge.api.IServerBridgeConsumer;
import com.gc.irc.server.bridge.api.IServerBridgeConsumerFactory;
import com.gc.irc.server.bridge.jms.utils.JMSConnectionUtils;

/**
 * The Class JMSConsumerFactory.
 */
@Component
@Scope("singleton")
public class JMSConsumerFactory extends AbstractLoggable implements IServerBridgeConsumerFactory {

    @Value("${jms.server.url}")
    private String brokerUrl;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.bridge.api.IServerBridgeConsumerFactory#getInstance()
     */
    @Override
    public IServerBridgeConsumer getInstance() {
        try {
            getLog().debug("Create JMS Consumer");
            return new JMSConsumer(JMSConnectionUtils.createConsumer(brokerUrl));
        } catch (final JMSException e) {
            getLog().error("Fail to create JMS Consumer : ", e);
        }
        return null;
    }

    /**
     * @param brokerUrl
     *            the brokerUrl to set
     */
    public void setBrokerUrl(final String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

}
