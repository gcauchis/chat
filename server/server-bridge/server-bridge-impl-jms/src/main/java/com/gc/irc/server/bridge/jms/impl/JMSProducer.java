package com.gc.irc.server.bridge.jms.impl;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.bridge.api.IServerBridgeProducer;
import com.gc.irc.server.bridge.api.ServerBridgeException;
import com.gc.irc.server.bridge.jms.utils.JMSConnectionUtils;

/**
 * Contain a JMSProducer to send a message in the JMS Queue.
 * 
 * @author gcauchis
 * 
 */
public class JMSProducer extends AbstractLoggable implements IServerBridgeProducer {

    /** The nb thread. */
    private static int nbThread = 0;

    /** The session. */
    private static Session session;;

    /**
     * Gets the nb thread.
     * 
     * @return the nb thread
     */
    protected static int getNbThread() {
        nbThread++;
        return nbThread;
    }

    /** The id. */
    private int id = getNbThread();

    /** The message producer. */
    private MessageProducer messageProducer = null;

    /**
     * Instantiates a new iRCJMS producer.
     */
    public JMSProducer(final String brokerUrl) {
        session = JMSConnectionUtils.getSession(brokerUrl);
        try {
            getLog().info(id + " Create the JMS producer");
            messageProducer = session.createProducer(JMSConnectionUtils.getQueue());
        } catch (final JMSException e) {
            getLog().error(id + " Fail to create the JMS Producer", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.bridge.api.IServerBridgeProducer#close()
     */
    @Override
    public void close() throws ServerBridgeException {
        try {
            messageProducer.close();
        } catch (final JMSException e) {
            getLog().error("fail to close message producer");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.jms.IJMSProducer#postInJMS(com.gc.irc.common.protocol
     * .IRCMessage)
     */
    @Override
    public void post(final Message objectMessage) throws ServerBridgeException {
        getLog().debug(id + " Send a message in JMS Queue.");
        ObjectMessage message = null;
        /**
         * Create the Message
         */
        try {
            getLog().debug(id + " Create JMS Message");
            message = session.createObjectMessage();
        } catch (final JMSException e) {
            getLog().warn(id + " Fail to create the message.", e);
        }

        /**
         * Write the Message
         */
        try {
            getLog().debug(id + " Write the Message");
            message.setObject(objectMessage);
        } catch (final JMSException e) {
            getLog().warn(id + " Fail to Write the message", e);
        }

        /**
         * Post the message.
         */
        try {
            getLog().debug(id + " Post Message in JMS");
            messageProducer.send(message);
        } catch (final JMSException e) {
            getLog().warn(id + " Fail to post the message : ", e);
        }
    }

}
