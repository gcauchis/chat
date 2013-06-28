package com.gc.irc.server.handler.message.impl;

import org.springframework.stereotype.Component;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.notice.MessageNoticeContactInfo;
import com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler;

/**
 * The Class IRCMessageNoticeContactInfoHandler.
 */
@Component
public class MessageNoticeContactInfoHandler extends AbstractServerMessageHandler<MessageNoticeContactInfo> {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler# internalHandle(com.gc.irc.common.protocol.IRCMessage)
     */
    @Override
    protected void internalHandle(final MessageNoticeContactInfo message) {
        final IRCUser userChange = message.getUser();
        // TODO : Persistence
        getLog().debug(" User " + userChange.getNickName() + " change state to " + userChange.getUserStatus() + " has pictur : " + userChange.hasPictur());
        sendToAllUsers(message);
    }

}
