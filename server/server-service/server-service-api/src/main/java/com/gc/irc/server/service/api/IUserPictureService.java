package com.gc.irc.server.service.api;

import com.gc.irc.common.api.ILoggable;
import com.gc.irc.common.protocol.item.IRCMessageItemPicture;

/**
 * Inteface to use Picture.
 * 
 * @author gcauchis
 * 
 */
public interface IUserPictureService extends ILoggable {

    /**
     * Add a picture for a designed User.
     * 
     * @param idUser
     *            Id of the User
     * @param image
     *            New Image
     * @return True if the Picture is Save successfully
     */
    boolean newPicture(int idUser, IRCMessageItemPicture image);

    /**
     * Get the Picture of the Designed User.
     * 
     * @param idUser
     *            Id of the User
     * @return The Picture of the user.
     */
    IRCMessageItemPicture getPictureOf(int idUser);

}
