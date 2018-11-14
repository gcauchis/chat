package com.gc.irc.server.core.user.management;

import java.util.List;

import com.gc.irc.common.entity.User;

/**
 * <p>IUserManagement interface.</p>
 *
 * @version 0.0.4
 * @author x472511
 */
public interface UserManagement {

    /**
     * Delete the deconnected Client.
     *
     * @param id a long.
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

    /**
     * <p>newUserConnected.</p>
     *
     * @param user a {@link com.gc.irc.common.entity.User} object.
     */
    void newUserConnected(User user);

    /**
     * <p>changeUserNickname.</p>
     *
     * @param id a long.
     * @param nickname a {@link java.lang.String} object.
     * @return a {@link com.gc.irc.common.entity.User} object.
     */
    User changeUserNickname(long id, String nickname);

    /**
     * <p>isLogged.</p>
     *
     * @param id a long.
     * @return a boolean.
     */
    boolean isLogged(long id);

    /**
     * <p>changeUserHasPicture.</p>
     *
     * @param fromId a long.
     */
    void changeUserHasPicture(long fromId);
}
