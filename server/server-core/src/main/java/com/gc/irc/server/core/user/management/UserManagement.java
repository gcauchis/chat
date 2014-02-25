package com.gc.irc.server.core.user.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.entity.User;
import com.gc.irc.server.persistance.PersiteUsers;

@Component("userManagement")
@Scope("singleton")
public class UserManagement extends AbstractLoggable implements IUserManagement {

    /** The list user by id. */
    private final Map<Long, User> listUserById = new ConcurrentHashMap<Long, User>();

    @Override
    public void disconnect(long id) {
        getLog().info("disconnect id={}", id);
        listUserById.remove(id);
        persistStatus();
    }

    private void persistStatus() {
        //TODO: Asynchronous
        /**
         * Persist the change.
         */
        PersiteUsers.persistListUser(getAllUsers());
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = null;
        synchronized (listUserById) {
            list = new ArrayList<User>(listUserById.values());
        }
        return list;
    }

    @Override
    public User getUser(final long id) {
        return listUserById.get(id);
    }

    @Override
    public void newUserConnected(User user) {
        listUserById.put(user.getId(), user);
        persistStatus();
    }

    @Override
    public User changeUserNickname(long id, String nickname) {
        User user = getUser(id);
        user.setNickName(nickname);
        persistStatus();
        return user;
    }

    @Override
    public boolean isLogged(long id) {
        return getUser(id) != null;
    }

    @Override
    public void changeUserHasPicture(long id) {
        User user = getUser(id);
        user.setHasPictur(true);
        persistStatus();
    }
}
