package com.gc.irc.server.handler.message;

import org.springframework.stereotype.Component;

import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.notice.MessageNoticeContactInfo;

/**
 * The Class IRCMessageNoticeContactInfoHandler.
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Component
public class MessageNoticeContactInfoHandler extends AbstractServerMessageHandler<MessageNoticeContactInfo> {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler# internalHandle(com.gc.irc.common.protocol.IRCMessage)
     */
    /** {@inheritDoc} */
    @Override
    protected void internalHandle(final MessageNoticeContactInfo message) {
        final User userChange = message.getUser();
        // TODO : Persistence
        getLog().debug(" User " + userChange.getNickName() + " change state to " + userChange.getUserStatus() + " has pictur : " + userChange.hasPictur());
        sendToAllUsers(message);
    }

}
