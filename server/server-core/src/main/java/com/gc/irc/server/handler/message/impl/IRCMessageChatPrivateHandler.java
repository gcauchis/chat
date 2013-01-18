package com.gc.irc.server.handler.message.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.chat.IRCMessageChatPrivate;
import com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler;
import com.gc.irc.server.jms.api.IJMSProducer;

/**
 * The Class IRCMessageChatPrivateHandler.
 */
@Component
public class IRCMessageChatPrivateHandler extends AbstractServerMessageHandler<IRCMessageChatPrivate> {

    /** The ijms producer. */
    @Autowired
    private IJMSProducer jmsProducer;

    /** The num passage max. */
    @Value("${nbMessageMaxPassage:10}")
    private int numPassageMax = 10;

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler#
     * internalHandle(com.gc.irc.common.protocol.IRCMessage)
     */
    @Override
    protected void internalHandle(final IRCMessageChatPrivate message) {
        final IRCUser sender = getSender(message);
        if (sender != null) {
            final IRCUser receiver = getUser(message.getToId());
            if (receiver != null) {
                getLog().debug(" Private Message from {} to {}", sender.getNickName(), receiver.getNickName());
                sendTo(message, message.getToId());
            } else {
                getLog().warn("inexisting destination id");
                getLog().debug("Check if retry later");
                final int numPassage = message.numPassage();
                if (numPassage < numPassageMax) {
                    getLog().debug("Send again the private message in JMS. Passage number {}", numPassage);
                    jmsProducer.postInJMS(message);
                } else {
                    getLog().debug("Message passed to much time in the server (more than {}). Trash it !", numPassageMax);
                }
            }
        } else {
            getLog().warn("inexisting source id");
        }

    }

    /**
     * Sets the jms producer.
     * 
     * @param jmsProducer
     *            the new jms producer
     */
    public void setjmsProducer(final IJMSProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }

    /**
     * Sets the num passage max.
     * 
     * @param numPassageMax
     *            the new num passage max
     */
    public void setNumPassageMax(final int numPassageMax) {
        this.numPassageMax = numPassageMax;
    }

}
