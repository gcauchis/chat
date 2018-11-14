package com.gc.irc.server.bridge.jms;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.server.bridge.ServerBridgeConsumer;
import com.gc.irc.server.bridge.ServerBridgeConsumerFactory;
import com.gc.irc.server.bridge.jms.utils.JMSConnectionUtils;

/**
 * The Class JMSConsumerFactory.
 *
 * @version 0.0.4
 * @author x472511
 */
@Component
@Scope("singleton")
public class JMSConsumerFactory extends AbstractLoggable implements ServerBridgeConsumerFactory {

    @Value("${jms.server.url}")
    private String brokerUrl;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.bridge.api.IServerBridgeConsumerFactory#getInstance()
     */
    /** {@inheritDoc} */
    @Override
    public ServerBridgeConsumer getInstance() {
        try {
            getLog().debug("Create JMS Consumer");
            return new JMSConsumer(JMSConnectionUtils.createConsumer(brokerUrl));
        } catch (final JMSException e) {
            getLog().error("Fail to create JMS Consumer : ", e);
        }
        return null;
    }

    /**
     * <p>Setter for the field <code>brokerUrl</code>.</p>
     *
     * @param brokerUrl
     *            the brokerUrl to set
     */
    public void setBrokerUrl(final String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

}
