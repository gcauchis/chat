package com.gc.irc.server.persistance;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import com.gc.irc.common.protocol.item.IRCMessageItemPicture;


/**
 * Manage the Users's Pictures.
 * @author gcauchis
 *
 */
public class IRCGestionPicture implements GestionPictureInterface{
	private static final Logger logger = Logger.getLogger(IRCGestionPicture.class);
	
	public static IRCGestionPicture getInstance() {
		return new IRCGestionPicture();
	}

	public synchronized IRCMessageItemPicture getPictureOf(int idUser) {
		logger.debug("Get pictur of "+idUser);
		IRCMessageItemPicture image = null;
		try {
			FileInputStream fichier = new FileInputStream(idUser+".ser");
			ObjectInputStream ois = new ObjectInputStream(fichier);
			image = (IRCMessageItemPicture) ois.readObject();
		} 
		catch (java.io.IOException e) {
			logger.warn("Fail to read pictur : "+e.getMessage()+"\n"+e.getStackTrace().toString());
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return image;
	}

	public synchronized boolean newPicture(int idUser, IRCMessageItemPicture image) {
		logger.debug("Add pictur");
		try {
		      FileOutputStream fichier = new FileOutputStream(idUser+".ser");
		      ObjectOutputStream oos = new ObjectOutputStream(fichier);
		      oos.writeObject(image);
		      oos.flush();
		      oos.close();
		    }
		    catch (java.io.IOException e) {
		    	logger.warn("Fail to write pictur : "+e.getMessage()+"\n"+e.getStackTrace().toString());
		    	e.printStackTrace();
		    	return false;
		    }
		return true;
	}
}
