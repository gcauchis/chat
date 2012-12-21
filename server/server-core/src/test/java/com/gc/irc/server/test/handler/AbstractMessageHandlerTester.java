package com.gc.irc.server.test.handler;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.message.api.IIRCMessageHandler;
import com.gc.irc.common.protocol.IRCMessage;

/**
 * The Class AbstractMessageHandler.
 */
public abstract class AbstractMessageHandlerTester extends AbstractLoggable implements IIRCMessageHandler {

    /** The message recieved. */
    boolean messageRecieved = false;

    /**
     * Instantiates a new abstract message handler.
     */
    public AbstractMessageHandlerTester() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.common.api.IIRCMessageHandler#handle(com.gc.irc.common.protocol
     * .IRCMessage)
     */
    @Override
    public void handle(final IRCMessage message) {
        getLog().info("Message recived: " + message);
        handleInternal(message);
        messageRecieved = true;
    }

    /**
     * Handle internal.
     * 
     * @param message
     *            the message
     */
    protected abstract void handleInternal(final IRCMessage message);

    /**
     * Checks if is message recieved.
     * 
     * @return true, if is message recieved
     */
    public boolean isMessageRecieved() {
        return messageRecieved;
    }

    /**
     * Reset.
     */
    public void reset() {
        getLog().info("Reset");
        messageRecieved = false;
        resetInsernal();
    }

    /**
     * Reset insernal.
     */
    protected abstract void resetInsernal();

}