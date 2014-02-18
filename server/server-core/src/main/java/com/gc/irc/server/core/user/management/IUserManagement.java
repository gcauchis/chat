package com.gc.irc.server.core.user.management;

import java.util.List;

import com.gc.irc.common.entity.User;

public interface IUserManagement {

    /**
     * Delete the deconnected Client.
     * 
     * @param client
     *            Deconnected Client.
     */
    void disconnect(long id);

    /**
     * Get the users Connected list.
     * 
     * @return The list of all the connected users.
     */
    List<User> getAllUsers();

    /**
     * Get the user demand if he is connected.
     * 
     * @param id
     *            User's Id.
     * @return The User selected or null if not find.
     */
    User getUser(final long id);

    void newUserConnected(User user);

    User changeUserNickname(long id, String nickname);

    boolean isLogged(long id);

    void changeUserHasPicture(long fromId);
}
