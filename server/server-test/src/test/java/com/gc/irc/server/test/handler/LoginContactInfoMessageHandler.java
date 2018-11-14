package com.gc.irc.server.test.handler;

import java.util.LinkedList;
import java.util.List;

import com.gc.irc.common.protocol.Message;
import com.gc.irc.common.protocol.notice.MessageNoticeContactInfo;

/**
 * The Class ContactInfoMessageHandler.
 *
 * @version 0.0.4
 * @since 0.0.4
 */
public class LoginContactInfoMessageHandler extends AbstractMessageHandlerTester {

    /** The message notice contact infos. */
    private final List<MessageNoticeContactInfo> messageNoticeContactInfos = new LinkedList<MessageNoticeContactInfo>();

    /**
     * Gets the message notice contact infos.
     *
     * @return the message notice contact infos
     */
    public List<MessageNoticeContactInfo> getMessageNoticeContactInfos() {
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
    /** {@inheritDoc} */
    @Override
    protected void handleInternal(Message message) {
        if (message instanceof MessageNoticeContactInfo) {
            messageNoticeContactInfos.add((MessageNoticeContactInfo) message);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.test.handler.AbstractMessageHandlerTester#resetInsernal()
     */
    /** {@inheritDoc} */
    @Override
    protected void resetInsernal() {
        messageNoticeContactInfos.clear();
    }

}
