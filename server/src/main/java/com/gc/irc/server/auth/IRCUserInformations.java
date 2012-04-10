package com.gc.irc.server.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.irc.common.entity.IRCUser;

/**
 * Represent a Client.
 * 
 * @author gcauchis
 * 
 */
public class IRCUserInformations {

    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IRCUserInformations.class);

    /** The id. */
    private int id;

    /** The nickname. */
    private String nickname;

    /** The login. */
    private String login;

    /** The password. */
    private String password;

    /** The has pictur. */
    private boolean hasPictur = false;

    /** The user. */
    private IRCUser user = null;

    /**
     * Class Builder.
     * 
     * @param id
     *            User's id.
     * @param nickname
     *            User's NickName.
     * @param login
     *            User's Login.
     * @param password
     *            User's Password.
     */
    public IRCUserInformations(final int id, final String nickname, final String login, final String password) {
        this.id = id;
        this.login = login;
        this.nickname = nickname;
        this.password = password;
        LOGGER.info("New users " + id + ": login : " + login);
    }

    /**
     * Class Builder.
     * 
     * @param id
     *            User's id.
     * @param nickname
     *            User's NickName.
     * @param login
     *            User's Login.
     * @param password
     *            User's Password.
     * @param hasPicture
     *            True if User have a Picture.
     */
    public IRCUserInformations(final int id, final String nickname, final String login, final String password, final boolean hasPicture) {
        this(id, nickname, login, password);
        hasPictur = hasPicture;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the login.
     * 
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Gets the nickname.
     * 
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the login.
     * 
     * @param login
     *            the new login
     */
    public void setLogin(final String login) {
        LOGGER.debug("Change login " + this.login + " to " + login);
        this.login = login;
        IRCServerAuthentification.getInstance().saveModification();
    }

    /**
     * Sets the nickname.
     * 
     * @param nickname
     *            the new nickname
     */
    public void setNickname(final String nickname) {
        LOGGER.debug("Change nickname " + this.nickname + " to " + nickname);
        this.nickname = nickname;
        if (user != null) {
            user.setNickName(nickname);
        }
        IRCServerAuthentification.getInstance().saveModification();
    }

    /**
     * Sets the password.
     * 
     * @param password
     *            the new password
     */
    public void setPassword(final String password) {
        LOGGER.debug("Change password " + this.password + " to " + password);
        this.password = password;
        IRCServerAuthentification.getInstance().saveModification();
    }

    /**
     * Compare login.
     * 
     * @param login
     *            Login to compare.
     * @return If equals true, else false.
     */
    public boolean loginEquals(final String login) {
        return this.login.equals(login);
    }

    /**
     * Compare Login and Password.
     * 
     * @param login
     *            Login to compare.
     * @param password
     *            Password to compare.
     * @return If Login and Password are good true, else false.
     */
    public boolean loginPasswordEquals(final String login, final String password) {
        return loginEquals(login) && this.password.equals(password);
    }

    /**
     * Generate xml.
     * 
     * @param tabulation
     *            Indentation.
     * @return xml in a String
     */
    public String toStringXML(final String tabulation) {
        final String result = tabulation + "<IRCUserInfo " + "id=\"" + id + "\" " + "nickname=\"" + nickname + "\" " + "login=\"" + login + "\" "
                + "password=\"" + password + "\" " + "hasPicture=\"" + hasPictur + "\" " + "/>\n";
        return result;
    }

    /**
     * Get the User object use to communicate with the other client.
     * 
     * @return An User.
     */
    public synchronized IRCUser getUser() {
        if (user == null) {
            user = new IRCUser(id, nickname, hasPictur);
        }
        return user;
    }

    /**
     * Inform if user have a personal Picture.
     * 
     * @return True if have a picture.
     */
    public boolean hasPictur() {
        return hasPictur;
    }

    /**
     * Sets the checks for picture.
     * 
     * @param havePicture
     *            the new checks for picture
     */
    public void setHasPicture(final boolean havePicture) {
        LOGGER.debug("Change havePicture " + hasPictur + " to " + havePicture);
        hasPictur = havePicture;
        if (user != null) {
            user.setHasPictur(havePicture);
        }
        IRCServerAuthentification.getInstance().saveModification();
    }

    /**
     * Checks if is connecte.
     * 
     * @return true, if is connecte
     */
    public boolean isConnecte() {
        return user != null;
    }

    /**
     * Deconnected.
     */
    public void deconnected() {
        LOGGER.debug(nickname + " is disconnected.");
        user = null;

    }
}
