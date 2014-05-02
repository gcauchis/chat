package com.gc.irc.server.core.user.management;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>UserManagementAware interface.</p>
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface UserManagementAware {
	
	/**
	 * Set the user management.
	 *
	 * @param userManagement the user management.
	 */
	@Autowired
	void setUserManagement(IUserManagement userManagement);

}
