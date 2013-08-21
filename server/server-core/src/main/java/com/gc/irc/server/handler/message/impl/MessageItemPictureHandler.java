package com.gc.irc.server.handler.message.impl;

import org.springframework.stereotype.Component;

import com.gc.irc.common.protocol.item.MessageItemPicture;
import com.gc.irc.server.handler.message.abs.AbstractServerCommandMessageHandler;
import com.gc.irc.server.model.UserInformations;

/**
 * The Class IRCMessageItemPictureHandler.
 */
@Component
public class MessageItemPictureHandler extends AbstractServerCommandMessageHandler<MessageItemPicture> {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.handler.message.abs.AbstractServerMessageHandler#internalHandle(com.gc.irc.common.protocol.IRCMessage)
     */
    @Override
    protected void internalHandle(MessageItemPicture message) {
        getUserPictureService().newPicture(message.getFromId(), message);

        final UserInformations userInfo = getAuthenticationService().getUser(message.getFromId());
        if (userInfo != null) {
            userInfo.setHasPicture(true);
            getAuthenticationService().update(userInfo);
            getUserManagement().changeUserHasPicture(message.getFromId());
        } else {
            getLog().warn("User null");
        }

        sendToAllUsers(message);
    }

}
