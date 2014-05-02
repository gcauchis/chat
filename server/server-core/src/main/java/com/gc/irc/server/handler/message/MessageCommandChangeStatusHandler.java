package com.gc.irc.server.handler.message;

import org.springframework.stereotype.Component;

import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.command.MessageCommandChangeStatus;
import com.gc.irc.common.protocol.notice.MessageNoticeContactInfo;

/**
 * The Class IRCMessageCommandChangeStatusHandler.
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Component
public class MessageCommandChangeStatusHandler extends AbstractServerCommandMessageHandler<MessageCommandChangeStatus> {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler#internalHandle(com.gc.irc.common.protocol.IRCMessage)
     */
    /** {@inheritDoc} */
    @Override
    protected void internalHandle(MessageCommandChangeStatus message) {
        final User user = getSender(message);
        if (user != null) {
            getLog().debug(user.getNickName() + " change status to " + message.getNewStatus());
            user.setUserStatus(message.getNewStatus());
            sendToAllUsers(new MessageNoticeContactInfo(user));
        }

    }

}
