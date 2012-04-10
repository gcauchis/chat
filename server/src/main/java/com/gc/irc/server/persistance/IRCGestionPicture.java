package com.gc.irc.server.persistance;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.irc.common.protocol.item.IRCMessageItemPicture;

/**
 * Manage the Users's Pictures.
 * 
 * @author gcauchis
 * 
 */
public class IRCGestionPicture implements GestionPictureInterface {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IRCGestionPicture.class);

    /**
     * Gets the single instance of IRCGestionPicture.
     * 
     * @return single instance of IRCGestionPicture
     */
    public static IRCGestionPicture getInstance() {
        return new IRCGestionPicture();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.persistance.GestionPictureInterface#getPictureOf(int)
     */
    public synchronized IRCMessageItemPicture getPictureOf(final int idUser) {
        LOGGER.debug("Get pictur of " + idUser);
        IRCMessageItemPicture image = null;
        try {
            final FileInputStream fichier = new FileInputStream(idUser + ".ser");
            final ObjectInputStream ois = new ObjectInputStream(fichier);
            image = (IRCMessageItemPicture) ois.readObject();
        } catch (final java.io.IOException e) {
            LOGGER.warn("Fail to read pictur : " + e.getMessage() + "\n" + e.getStackTrace().toString());
            e.printStackTrace();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
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
    public synchronized boolean newPicture(final int idUser, final IRCMessageItemPicture image) {
        LOGGER.debug("Add pictur");
        try {
            final FileOutputStream fichier = new FileOutputStream(idUser + ".ser");
            final ObjectOutputStream oos = new ObjectOutputStream(fichier);
            oos.writeObject(image);
            oos.flush();
            oos.close();
        } catch (final java.io.IOException e) {
            LOGGER.warn("Fail to write pictur : " + e.getMessage() + "\n" + e.getStackTrace().toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
