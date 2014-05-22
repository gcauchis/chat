package com.gc.irc.server.core.user.management;

import java.io.IOException;
import java.io.ObjectOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.item.MessageItemPicture;
import com.gc.irc.common.utils.IOStreamUtils;
import com.gc.irc.server.service.UserPictureService;

/**
 * <p>UserPicturesManagement class.</p>
 *
 * @author gcauchis
 * @version 0.0.4
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
	public void sendUsersPictures(ObjectOutputStream outObject) {
		 MessageItemPicture messagePicture;
	        for (final User user : userManagement.getAllUsers()) {
	            if (user.hasPictur()) {
	                messagePicture = userPictureService.getPictureOf(user.getId());
	                if (messagePicture != null) {
	                    try {
	                        IOStreamUtils.sendMessage(outObject, messagePicture);
	                    } catch (final IOException e) {
	                        getLog().warn("Fail to send the picture of " + user.getNickName() + " : ", e);
	                    }
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
