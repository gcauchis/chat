package com.gc.irc.server.core.user.management.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.entity.User;
import com.gc.irc.server.core.user.management.api.IUserManagement;
import com.gc.irc.server.persistance.PersiteUsers;

@Component("userManagement")
@Scope("singleton")
public class UserManagement extends AbstractLoggable implements IUserManagement{
	
	 /** The list user by id. */
    private final Map<Integer, User> listUserById = new ConcurrentHashMap<Integer, User>();
    
    public void disconnect(int id) {
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
    public User getUser(final int id) {
        return listUserById.get(id);
    }

	@Override
	public void newUserConnected(User user) {
		listUserById.put(user.getId(), user);
		persistStatus();
	}

	@Override
	public User changeUserNickname(int id, String nickname) {
		User user = getUser(id);
		user.setNickName(nickname);
		persistStatus();
		return user;
	}

	@Override
	public boolean isLogged(int id) {
		return getUser(id) != null;
	}

	@Override
	public void changeUserHasPicture(int id) {
		User user = getUser(id);
		user.setHasPictur(true);
		persistStatus();
	}

}
