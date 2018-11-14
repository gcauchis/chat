package com.gc.irc.server.core.user.management;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.entity.User;
import com.gc.irc.common.message.MessageSender;
import com.gc.irc.common.protocol.item.MessageItemPicture;
import com.gc.irc.server.core.user.management.UserManagement;
import com.gc.irc.server.core.user.management.UserManagementAware;
import com.gc.irc.server.core.user.management.UserPicturesManagement;
import com.gc.irc.server.service.UserPictureService;

/**
 * <p>UserPicturesManagement class.</p>
 *
 * @version 0.0.4
 * @author x472511
 */
@Component("userPicturesManagement")
public class DefaultUserPicturesManagement extends AbstractLoggable implements UserPicturesManagement, UserManagementAware {

	/** The user picture service. */
    @Autowired
    private UserPictureService userPictureService;
    
    /** The user management */
    private UserManagement userManagement;
	
	/** {@inheritDoc} */
	@Override
	public void sendUsersPictures(final MessageSender messageSender) {
		 MessageItemPicture messagePicture;
	        for (final User user : userManagement.getAllUsers()) {
	            if (user.hasPictur()) {
	                messagePicture = userPictureService.getPictureOf(user.getId());
	                if (messagePicture != null) {
	                	messageSender.send(messagePicture);
	                } else {
	                    getLog().warn("Fail to open the picture of {}", user.getNickName());
	                }
	            }
	        }
	}
	
    /**
     * Sets the user picture service.
     *
     * @param userPictureService
     *            the new user picture service
     */
    public void setUserPictureService(final UserPictureService userPictureService) {
        this.userPictureService = userPictureService;
    }

	/** {@inheritDoc} */
	@Override
	@Autowired
	public void setUserManagement(UserManagement userManagement) {
		this.userManagement = userManagement;
	}

}
