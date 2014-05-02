package com.gc.irc.server.service;

import com.gc.irc.common.ILoggable;
import com.gc.irc.common.protocol.item.MessageItemPicture;

/**
 * Inteface to use Picture.
 *
 * @author gcauchis
 * @version 0.0.4
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
    boolean newPicture(long idUser, MessageItemPicture image);

    /**
     * Get the Picture of the Designed User.
     *
     * @param idUser
     *            Id of the User
     * @return The Picture of the user.
     */
    MessageItemPicture getPictureOf(long idUser);

}
