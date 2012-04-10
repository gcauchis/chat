package com.gc.irc.server.persistance;

import com.gc.irc.common.protocol.item.IRCMessageItemPicture;

/**
 * Inteface to use Picture.
 * 
 * @author gcauchis
 * 
 */
public interface GestionPictureInterface {

    /**
     * Add a picture for a designed User.
     * 
     * @param idUser
     *            Id of the User
     * @param image
     *            New Image
     * @return True if the Picture is Save successfully
     */
    public boolean newPicture(int idUser, IRCMessageItemPicture image);

    /**
     * Get the Picture of the Designed User.
     * 
     * @param idUser
     *            Id of the User
     * @return The Picture of the user.
     */
    public IRCMessageItemPicture getPictureOf(int idUser);

}
