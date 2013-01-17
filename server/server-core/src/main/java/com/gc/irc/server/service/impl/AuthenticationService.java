package com.gc.irc.server.service.impl;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.item.IRCMessageItemPicture;
import com.gc.irc.common.utils.IOStreamUtils;
import com.gc.irc.common.utils.IOUtils;
import com.gc.irc.server.entity.UserInformations;
import com.gc.irc.server.service.api.IAuthenticationService;
import com.gc.irc.server.service.api.IUserPictureService;
import com.gc.irc.server.service.utils.UserInformationScanner;

/**
 * Singleton class use for login and register all the users.
 * 
 * @author gcauchis
 * 
 */
@Component("authenticationService")
@Scope("singleton")
public class AuthenticationService extends AbstractLoggable implements IAuthenticationService {

    /** The last id. */
    private static int lastId;

    /**
     * Sets the last id.
     * 
     * @param lastId
     *            the new last id
     */
    private static void setLastId(final int lastId) {
        AuthenticationService.lastId = lastId;
    }

    /** The list users. */
    private final List<UserInformations> listUsers;

    /** The path fichier. */
    private final String pathFichier = "auth.xml";

    /** The user picture service. */
    @Autowired
    private IUserPictureService userPictureService;

    /**
     * Read the Users data.
     */
    private AuthenticationService() {
        getLog().debug("Read the Users data.");
        try {
            new UserInformationScanner(pathFichier);
        } catch (final ParserConfigurationException e) {
            getLog().warn("Fail to parse xml.", e);
        } catch (final SAXException e) {
            getLog().warn("Fail to parse xml.", e);
        } catch (final IOException e) {
            getLog().warn("Fail to read xml file. If file didn't exist yet, don't worry with this error", e);
        }
        setLastId(UserInformationScanner.getLastId());
        listUsers = UserInformationScanner.getListUserInfomation();
        getLog().debug("End init auth.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.auth.AuthentificationInterface#addUser(java.lang.String
     * , java.lang.String, java.lang.String)
     */
    @Override
    public boolean addUser(final String login, final String password, final String nickname) {
        if (userLoginExist(login)) {
            getLog().info("Login already exist");
            return false;
        }
        listUsers.add(new UserInformations(getNewId(), nickname, login, password));
        saveModification();
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.auth.AuthentificationInterface#changeNickUser(int,
     * java.lang.String)
     */
    @Override
    public void changeNickUser(final int id, final String nickname) {
        for (final UserInformations user : listUsers) {
            if (user.getId() == id) {
                user.setNickname(nickname);
                saveModification();
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
    @Override
    public void changePasswordUser(final int id, final String password) {
        for (final UserInformations user : listUsers) {
            if (user.getId() == id) {
                user.setPassword(password);
                saveModification();
                return;
            }
        }
    }

    /**
     * Generate xml code.
     * 
     * @return xml code in a String.
     */
    private String generateXML() {
        String result = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\" ?>\n\n";
        result += "<IRCUsers lastId=\"" + lastId + "\">\n";
        for (final UserInformations user : listUsers) {
            result += user.toStringXML("\t");
        }

        result += "</IRCUsers>\n";

        return result;
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

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.auth.AuthentificationInterface#getUser(int)
     */
    @Override
    public UserInformations getUser(final int id) {
        for (final UserInformations user : listUsers) {
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
     * com.gc.irc.server.auth.AuthentificationInterface#logUser(java.lang.String
     * , java.lang.String)
     */
    @Override
    public IRCUser logUser(final String login, final String password) {

        for (final UserInformations user : listUsers) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                if (user.isConnected()) {
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
     * @see com.gc.irc.server.auth.IAuthentificationService#saveModification()
     */
    @Override
    public void saveModification() {
        synchronized (listUsers) {
            IOUtils.writeFile(pathFichier, generateXML());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.auth.AuthentificationInterface#sendUsersPicture(java
     * .io.ObjectOutputStream)
     */
    @Override
    public synchronized void sendUsersPicture(final ObjectOutputStream outObject) {
        IRCMessageItemPicture messagePicture;
        for (final UserInformations user : listUsers) {
            if (user.isConnected() && user.hasPictur()) {
                messagePicture = userPictureService.getPictureOf(user.getId());
                if (messagePicture != null) {
                    try {
                        IOStreamUtils.sendMessage(outObject, messagePicture);
                    } catch (final IOException e) {
                        getLog().warn("Fail to send the picture of " + user.getNickname() + " : ", e);
                    }
                } else {
                    getLog().warn("Fail to open the picture of " + user.getNickname());
                }
            }
        }
    }

    /**
     * Sets the user picture service.
     * 
     * @param userPictureService
     *            the new user picture service
     */
    public void setUserPictureService(final IUserPictureService userPictureService) {
        this.userPictureService = userPictureService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.auth.AuthentificationInterface#userLoginExist(java.
     * lang.String)
     */
    @Override
    public boolean userLoginExist(final String login) {
        for (final UserInformations user : listUsers) {
            if (user.loginEquals(login)) {
                return true;
            }
        }
        return false;
    }
}
