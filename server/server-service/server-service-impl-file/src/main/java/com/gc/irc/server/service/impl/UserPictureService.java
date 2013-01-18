package com.gc.irc.server.service.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Service;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.protocol.item.IRCMessageItemPicture;
import com.gc.irc.server.service.api.IUserPictureService;

/**
 * Manage the Users's Pictures.
 * 
 * @author gcauchis
 * 
 */
@Service("userPictureManagement")
public class UserPictureService extends AbstractLoggable implements IUserPictureService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.persistance.GestionPictureInterface#getPictureOf(int)
     */
    @Override
    public synchronized IRCMessageItemPicture getPictureOf(final int idUser) {
        getLog().debug("Get pictur of " + idUser);
        IRCMessageItemPicture image = null;
        try {
            final FileInputStream fichier = new FileInputStream(idUser + ".ser");
            final ObjectInputStream ois = new ObjectInputStream(fichier);
            image = (IRCMessageItemPicture) ois.readObject();
        } catch (final java.io.IOException e) {
            getLog().warn("Fail to read pictur:", e);
        } catch (final ClassNotFoundException e) {
            getLog().warn("Fail to read object:", e);
        }
        return image;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.persistance.GestionPictureInterface#newPicture(int,
     * com.gc.irc.common.protocol.item.IRCMessageItemPicture)
     */
    @Override
    public synchronized boolean newPicture(final int idUser, final IRCMessageItemPicture image) {
        getLog().debug("Add pictur");
        try {
            final FileOutputStream fichier = new FileOutputStream(idUser + ".ser");
            final ObjectOutputStream oos = new ObjectOutputStream(fichier);
            oos.writeObject(image);
            oos.flush();
            oos.close();
        } catch (final java.io.IOException e) {
            getLog().warn("Fail to write pictur:", e);
            return false;
        }
        return true;
    }
}
