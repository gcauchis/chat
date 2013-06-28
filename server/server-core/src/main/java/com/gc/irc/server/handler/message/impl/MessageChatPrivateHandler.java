package com.gc.irc.server.handler.message.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.chat.IRCMessageChatPrivate;
import com.gc.irc.server.bridge.api.IServerBridgeProducer;
import com.gc.irc.server.bridge.api.ServerBridgeException;
import com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler;

/**
 * The Class IRCMessageChatPrivateHandler.
 */
@Component
public class MessageChatPrivateHandler extends AbstractServerMessageHandler<IRCMessageChatPrivate> {

    /** The num passage max. */
    @Value("${nbMessageMaxPassage:10}")
    private int numPassageMax = 10;

    /** The ijms producer. */
    @Autowired
    private IServerBridgeProducer serverBridgeProducer;

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler# internalHandle(com.gc.irc.common.protocol.IRCMessage)
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
                    try {
                        serverBridgeProducer.post(message);
                    } catch (final ServerBridgeException e) {
                        getLog().error("Fail to resend private message", e);
                    }
                } else {
                    getLog().debug("Message passed to much time in the server (more than {}). Trash it !", numPassageMax);
                }
            }
        } else {
            getLog().warn("inexisting source id");
        }

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

    /**
     * Sets the server bridge producer.
     * 
     * @param serverBridgeProducer
     *            the new server bridge producer
     */
    public void setServerBridgeProducer(IServerBridgeProducer serverBridgeProducer) {
        this.serverBridgeProducer = serverBridgeProducer;
    }

}