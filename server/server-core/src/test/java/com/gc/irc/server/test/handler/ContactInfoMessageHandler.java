package com.gc.irc.server.test.handler;

import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.notice.IRCMessageNoticeContactInfo;

/**
 * The Class ContactInfoMessageHandler.
 */
public class ContactInfoMessageHandler extends AbstractMessageHandlerTester {

    /** The nb contact info received. */
    private int nbContactInfoReceived = 0;

    /**
     * Gets the nb contact info received.
     * 
     * @return the nb contact info received
     */
    public int getNbContactInfoReceived() {
        return nbContactInfoReceived;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.test.handler.AbstractMessageHandlerTester#handleInternal(com.gc.irc.common.protocol.IRCMessage)
     */
    @Override
    protected void handleInternal(IRCMessage message) {
        if (message instanceof IRCMessageNoticeContactInfo) {
            nbContactInfoReceived++;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.test.handler.AbstractMessageHandlerTester#resetInsernal()
     */
    @Override
    protected void resetInsernal() {
        nbContactInfoReceived = 0;
    }

}
