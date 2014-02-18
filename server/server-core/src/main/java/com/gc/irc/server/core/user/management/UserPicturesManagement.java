package com.gc.irc.server.core.user.management;

import java.io.IOException;
import java.io.ObjectOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.item.MessageItemPicture;
import com.gc.irc.common.utils.IOStreamUtils;
import com.gc.irc.server.service.IUserPictureService;

@Component("userPicturesManagement")
public class UserPicturesManagement extends AbstractLoggable implements IUserPicturesManagement, UserManagementAware {

	/** The user picture service. */
    @Autowired
    private IUserPictureService userPictureService;
    
    /** The user management */
    private IUserManagement userManagement;
	
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
    public void setUserPictureService(final IUserPictureService userPictureService) {
        this.userPictureService = userPictureService;
    }

	@Override
	@Autowired
	public void setUserManagement(IUserManagement userManagement) {
		this.userManagement = userManagement;
	}

}
