package com.gc.irc.server.test.handler;

import java.util.ArrayList;
import java.util.List;

import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.notice.IRCMessageNoticeContactInfo;

/**
 * The Class ContactInfoMessageHandler.
 */
public class LoginContactInfoMessageHandler extends AbstractMessageHandlerTester {

    /** The message notice contact infos. */
    private final List<IRCMessageNoticeContactInfo> messageNoticeContactInfos = new ArrayList<IRCMessageNoticeContactInfo>();

    /**
     * Gets the message notice contact infos.
     * 
     * @return the message notice contact infos
     */
    public List<IRCMessageNoticeContactInfo> getMessageNoticeContactInfos() {
        return messageNoticeContactInfos;
    }

    /**
     * Gets the nb contact info received.
     * 
     * @return the nb contact info received
     */
    public int getNbContactInfoReceived() {
        return messageNoticeContactInfos.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.test.handler.AbstractMessageHandlerTester#handleInternal(com.gc.irc.common.protocol.IRCMessage)
     */
    @Override
    protected void handleInternal(IRCMessage message) {
        if (message instanceof IRCMessageNoticeContactInfo) {
            messageNoticeContactInfos.add((IRCMessageNoticeContactInfo) message);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.test.handler.AbstractMessageHandlerTester#resetInsernal()
     */
    @Override
    protected void resetInsernal() {
        messageNoticeContactInfos.clear();
    }

}
