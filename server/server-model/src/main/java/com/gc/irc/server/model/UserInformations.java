package com.gc.irc.server.model;

import com.gc.irc.common.abs.AbstractLoggable;

/**
 * Represent a Client.
 * 
 * @author gcauchis
 * 
 */
public class UserInformations extends AbstractLoggable {

    /** The has pictur. */
    private boolean hasPictur = false;

    /** The id. */
    private final int id;

    /** The login. */
    private String login;

    /** The nickname. */
    private String nickname;

    /** The password. */
    private String password;

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
    public UserInformations(final int id, final String nickname, final String login, final String password) {
        this.id = id;
        this.login = login;
        this.nickname = nickname;
        this.password = password;
        getLog().debug("New users " + id + ": login : " + login);
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
    public UserInformations(final int id, final String nickname, final String login, final String password, final boolean hasPicture) {
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
     * Inform if user have a personal Picture.
     * 
     * @return True if have a picture.
     */
    public boolean hasPictur() {
        return hasPictur;
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
     * Sets the checks for picture.
     * 
     * @param havePicture
     *            the new checks for picture
     */
    public void setHasPicture(final boolean havePicture) {
        getLog().debug("Change havePicture " + hasPictur + " to " + havePicture);
        hasPictur = havePicture;
    }

    /**
     * Sets the login.
     * 
     * @param login
     *            the new login
     */
    public void setLogin(final String login) {
        getLog().debug("Change login " + this.login + " to " + login);
        this.login = login;
    }

    /**
     * Sets the nickname.
     * 
     * @param nickname
     *            the new nickname
     */
    public void setNickname(final String nickname) {
        getLog().debug("Change nickname " + this.nickname + " to " + nickname);
        this.nickname = nickname;
    }

    /**
     * Sets the password.
     * 
     * @param password
     *            the new password
     */
    public void setPassword(final String password) {
        getLog().debug("Change password " + this.password + " to " + password);
        this.password = password;
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
}
