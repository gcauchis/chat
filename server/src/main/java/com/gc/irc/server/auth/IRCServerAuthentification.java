package com.gc.irc.server.auth;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.item.IRCMessageItemPicture;
import com.gc.irc.common.utils.IOStreamUtils;
import com.gc.irc.common.utils.IOUtils;
import com.gc.irc.server.persistance.IRCGestionPicture;

/**
 * Singleton class use for login and register all the users.
 * 
 * @author gcauchis
 * 
 */
public class IRCServerAuthentification implements AuthentificationInterface {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IRCServerAuthentification.class);

    /** The instance. */
    private static IRCServerAuthentification instance;

    /** The list users. */
    private List<IRCUserInformations> listUsers;

    /** The path fichier. */
    private String pathFichier = "auth.xml";

    /** The last id. */
    private static int lastId;

    /**
     * Sets the last id.
     * 
     * @param lastId
     *            the new last id
     */
    private static void setLastId(final int lastId) {
        IRCServerAuthentification.lastId = lastId;
    }

    /**
     * Gets the new id.
     * 
     * @return the new id
     */
    private int getNewId() {
        lastId++;
        return lastId;
    }

    /**
     * Read the Users data.
     */
    private IRCServerAuthentification() {
        LOGGER.debug("Read the Users data.");
        try {
            new UserImformationScanner(pathFichier);
        } catch (final ParserConfigurationException e) {
            LOGGER.warn("Fail to parse xml.", e);
        } catch (final SAXException e) {
            LOGGER.warn("Fail to parse xml.", e);
        } catch (final IOException e) {
            LOGGER.warn("Fail to read xml file. If file didn't exist yet, don't worry with this error", e);
        }
        setLastId(UserImformationScanner.getLastId());
        listUsers = UserImformationScanner.getListUserInfomation();
        LOGGER.debug("End init auth.");
    }

    /**
     * Get an instance of the class.
     * 
     * @return The unique instance of the Class.
     */
    public static synchronized IRCServerAuthentification getInstance() {
        if (instance == null) {
            instance = new IRCServerAuthentification();
        }
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.auth.AuthentificationInterface#addUser(java.lang.String
     * , java.lang.String, java.lang.String)
     */
    public boolean addUser(final String login, final String password, final String nickname) {
        if (userLoginExist(login)) {
            LOGGER.info("Login already exist");
            return false;
        }
        listUsers.add(new IRCUserInformations(getNewId(), nickname, login, password));
        saveModification();
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.auth.AuthentificationInterface#userLoginExist(java.
     * lang.String)
     */
    public boolean userLoginExist(final String login) {
        for (final IRCUserInformations user : listUsers) {
            if (user.loginEquals(login)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generate xml code.
     * 
     * @return xml code in a String.
     */
    private String generateXML() {
        String result = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\" ?>\n\n";
        result += "<IRCUsers lastId=\"" + lastId + "\">\n";
        for (final IRCUserInformations user : listUsers) {
            result += user.toStringXML("\t");
        }

        result += "</IRCUsers>\n";

        return result;
    }

    /**
     * Persist the modification.
     */
    public void saveModification() {
        synchronized (listUsers) {
            IOUtils.writeFile(pathFichier, generateXML());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.auth.AuthentificationInterface#logUser(java.lang.String
     * , java.lang.String)
     */
    public IRCUser logUser(final String login, final String password) {

        for (final IRCUserInformations user : listUsers) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                if (user.isConnecte()) {
                    return null;
                }
                return user.getUser();
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.auth.AuthentificationInterface#changeNickUser(int,
     * java.lang.String)
     */
    public void changeNickUser(final int id, final String nickname) {
        for (final IRCUserInformations user : listUsers) {
            if (user.getId() == id) {
                user.setNickname(nickname);
                return;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.auth.AuthentificationInterface#changePasswordUser(int,
     * java.lang.String)
     */
    public void changePasswordUser(final int id, final String password) {
        for (final IRCUserInformations user : listUsers) {
            if (user.getId() == id) {
                user.setPassword(password);
                return;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.auth.AuthentificationInterface#getUser(int)
     */
    public IRCUserInformations getUser(final int id) {
        for (final IRCUserInformations user : listUsers) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.auth.AuthentificationInterface#sendUsersPicture(java
     * .io.ObjectOutputStream)
     */
    public synchronized void sendUsersPicture(final ObjectOutputStream outObject) {
        IRCMessageItemPicture messagePicture;
        for (final IRCUserInformations user : listUsers) {
            if (user.isConnecte() && user.hasPictur()) {
                final IRCGestionPicture picturUser = IRCGestionPicture.getInstance();
                messagePicture = picturUser.getPictureOf(user.getId());
                if (messagePicture != null) {
                    try {
                        IOStreamUtils.sendMessage(outObject, messagePicture);
                    } catch (final IOException e) {
                        LOGGER.warn("Fail to send the picture of " + user.getNickname() + " : ", e);
                    }
                } else {
                    LOGGER.warn("Fail to open the picture of " + user.getNickname());
                }
            }
        }
    }
}
