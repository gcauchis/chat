package com.gc.irc.server.bridge.jms;

import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.bridge.ServerBridgeConsumer;
import com.gc.irc.server.bridge.ServerBridgeException;

/**
 * The Class JMSConsumer.
 *
 * @version 0.0.4
 * @author x472511
 */
public class JMSConsumer extends AbstractLoggable implements ServerBridgeConsumer {

    /** The message consumer. */
    private MessageConsumer messageConsumer;

    /**
     * The Constructor.
     *
     * @param messageConsumer
     *            the message consumer
     */
    public JMSConsumer(final MessageConsumer messageConsumer) {
        super();
        this.messageConsumer = messageConsumer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.bridge.api.IServerBridgeConsumer#close()
     */
    /** {@inheritDoc} */
    @Override
    public void close() throws ServerBridgeException {
        try {
            messageConsumer.close();
        } catch (final JMSException e) {
            throw new ServerBridgeException("Fail to close message consumer", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.bridge.api.IServerBridgeConsumer#receive()
     */
    /** {@inheritDoc} */
    @Override
    public Message receive() throws ServerBridgeException {

        ObjectMessage message = null;

        try {
            getLog().debug("Wait for a message in JMS Queue");
            message = (ObjectMessage) messageConsumer.receive();
        } catch (final IllegalStateException e) {
            getLog().error("JMS in bad State", e);
            System.exit(-1);
        } catch (final JMSException e) {
            throw new ServerBridgeException("Fail to receive message", e);
        }

        Message messageObj = null;
        try {
            getLog().debug("Extract Message receive in JMS");
            messageObj = (Message) message.getObject();
        } catch (final JMSException e) {
            throw new ServerBridgeException("Fail final to extract Message final receive in JMS : ", e);
        }

        return messageObj;
    }
}
