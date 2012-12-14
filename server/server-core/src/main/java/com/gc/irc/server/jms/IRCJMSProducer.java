package com.gc.irc.server.jms;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.protocol.IRCMessage;

/**
 * Contain a JMSProducer to send a message in the JMS Queue.
 * 
 * @author gcauchis
 * 
 */
public class IRCJMSProducer extends AbstractLoggable {

    /** The nb thread. */
    private static int nbThread = 0;

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

    /** The session. */
    private static Session session = JMSConnection.getSession();

    /** The message producer. */
    private MessageProducer messageProducer = null;

    /**
     * Instantiates a new iRCJMS producer.
     */
    public IRCJMSProducer() {
        try {
            getLog().info(id + " Create the JMS producer");
            messageProducer = session.createProducer(JMSConnection.getQueue());
        } catch (final JMSException e) {
            getLog().error(id + " Fail to create the JMS Producer", e);
        }
    }

    /**
     * Send a message in the JMS Queue.
     * 
     * @param objectMessage
     *            the object message
     */
    public void postMessageObjectInJMS(final IRCMessage objectMessage) {
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
