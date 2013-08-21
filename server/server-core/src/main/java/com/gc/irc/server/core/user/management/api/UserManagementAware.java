package com.gc.irc.server.core.user.management.api;

import org.springframework.beans.factory.annotation.Autowired;

public interface UserManagementAware {
	
	/**
	 * Set the user management.
	 * @param userManagement the user management.
	 */
	@Autowired
	void setUserManagement(IUserManagement userManagement);

}
