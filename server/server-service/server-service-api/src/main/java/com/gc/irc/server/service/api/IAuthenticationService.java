package com.gc.irc.server.service.api;

import java.io.ObjectOutputStream;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.server.model.UserInformations;

/**
 * Interface use for the login of the users.
 * 
 * @author gcauchis
 * 
 */
public interface IAuthenticationService {

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
    boolean addUser(String login, String password, String nickname);

    /**
     * Change the Nickname of the User.
     * 
     * @param id
     *            User's id.
     * @param nickname
     *            New NickName
     */
    void updateUserNickName(int id, String nickname);

    /**
     * Change the Password of the User.
     * 
     * @param id
     *            User's id.
     * @param password
     *            New Password.
     */
    void updateUserPasword(int id, String password);

    /**
     * Get the designed User.
     * 
     * @param id
     *            User's id.
     * @return The User if exist. Else null.
     */
    UserInformations getUser(int id);

    /**
     * Log an user.
     * 
     * @param login
     *            User's Login.
     * @param password
     *            User's Password.
     * @return The user if login succeed. Null if login fail.
     */
    IRCUser logUser(String login, String password);
    
    
    /**
     * Update.
     *
     * @param userInformations the user informations
     */
    void update(UserInformations userInformations);

    /**
     * Send in the ObjectOutputStream the Picture of all the connected Users.
     * 
     * @param outObject
     *            ObjectOutputStream of the new Client.
     */
    void sendUsersPicture(ObjectOutputStream outObject);

    /**
     * Test if new login is free.
     * 
     * @param login
     *            Login to add.
     * @return True if login already exist, else false.
     */
    boolean userLoginExist(String login);
}
