package com.gc.irc.server.service;

import com.gc.irc.server.model.UserInformations;

/**
 * Interface use for the login of the users.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface AuthenticationService {

    /**
     * Add a new user.
     *
     * @param login
     *            User's Login.
     * @param password
     *            User's Password.
     * @param nickname
     *            User's NickName
     * @return If user add true, else false.
     */
    boolean addNewUser(String login, String password, String nickname);

    /**
     * Change the Nickname of the User.
     *
     * @param id
     *            User's id.
     * @param nickname
     *            New NickName
     */
    void updateUserNickName(long id, String nickname);

    /**
     * Change the Password of the User.
     *
     * @param id
     *            User's id.
     * @param password
     *            New Password.
     */
    void updateUserPasword(long id, String password);

    /**
     * Get the designed User.
     *
     * @param id
     *            User's id.
     * @return The User if exist. Else null.
     */
    UserInformations getUser(long id);
    
    /**
     * Delete the user.
     *
     * @param id the user id.
     */
    void delete(long id);

    /**
     * Log an user.
     *
     * @param login
     *            User's Login.
     * @param password
     *            User's Password.
     * @return The user if login succeed. Null if login fail.
     */
    UserInformations logUser(String login, String password);
    
    /**
     * Update.
     *
     * @param userInformations the user informations
     */
    void update(UserInformations userInformations);

    /**
     * Test if new login is free.
     *
     * @param login
     *            Login to add.
     * @return True if login already exist, else false.
     */
    boolean userLoginExist(String login);
    
    /**
     * Builds the anonymous id.
     *
     * @return the long
     */
    long buildAnonymousId();
}
