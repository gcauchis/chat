package com.gc.irc.server.test.handler;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.protocol.Message;

/**
 * The Class AbstractMessageHandler.
 *
 * @author gcauchis
 * @version 0.0.4
 * @since 0.0.4
 */
public abstract class AbstractMessageHandlerTester extends AbstractLoggable implements IMessageHandlerTester {

    /** The last received message. */
    private Message lastReceivedMessage;

    /**
     * Instantiates a new abstract message handler.
     */
    public AbstractMessageHandlerTester() {
        super();
    }

    /**
     * {@inheritDoc}
     *
     * Gets the last received message.
     */
    @Override
    public Message getLastReceivedMessage() {
        return lastReceivedMessage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.common.message.api.IIRCMessageHandler#handle(com.gc.irc.common .protocol.IRCMessage)
     */
    /** {@inheritDoc} */
    @Override
    public void handle(final Message message) {
        getLog().info("Message recived: " + message);
        lastReceivedMessage = message;
        handleInternal(message);
    }

    /**
     * Handle internal.
     *
     * @param message
     *            the message
     */
    protected abstract void handleInternal(final Message message);

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.test.handler.IMessageHandlerTester#isMessageRecieved()
     */
    /** {@inheritDoc} */
    @Override
    public boolean isMessageRecieved() {
        return lastReceivedMessage != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.test.handler.IMessageHandlerTester#reset()
     */
    /** {@inheritDoc} */
    @Override
    public void reset() {
        getLog().debug("Reset");
        lastReceivedMessage = null;
        resetInsernal();
    }

    /**
     * Reset insernal.
     */
    protected abstract void resetInsernal();

}
