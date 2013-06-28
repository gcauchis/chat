package com.gc.irc.server.test.handler;

import com.gc.irc.common.protocol.Message;

/**
 * The Class SimpleMessageHandler.
 */
public class SimpleMessageHandler extends AbstractMessageHandlerTester {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.test.handler.AbstractMessageHandler#handleInternal(
     * com.gc.irc.common.protocol.IRCMessage)
     */
    @Override
    protected void handleInternal(final Message message) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.test.handler.AbstractMessageHandler#resetInsernal()
     */
    @Override
    protected void resetInsernal() {
    }

}
