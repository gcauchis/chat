package com.gc.irc.server.test.handler;

import com.gc.irc.common.protocol.Message;

/**
 * The Class SimpleMessageHandler.
 *
 * @version 0.0.4
 * @since 0.0.4
 */
public class SimpleMessageHandler extends AbstractMessageHandlerTester {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.test.handler.AbstractMessageHandler#handleInternal(
     * com.gc.irc.common.protocol.IRCMessage)
     */
    /** {@inheritDoc} */
    @Override
    protected void handleInternal(final Message message) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.test.handler.AbstractMessageHandler#resetInsernal()
     */
    /** {@inheritDoc} */
    @Override
    protected void resetInsernal() {
    }

}
