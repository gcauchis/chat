package com.gc.irc.server.bridge.jms.impl;

import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.bridge.api.IServerBridgeConsumer;
import com.gc.irc.server.bridge.api.ServerBridgeException;

/**
 * The Class JMSConsumer.
 */
public class JMSConsumer extends AbstractLoggable implements IServerBridgeConsumer {

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
