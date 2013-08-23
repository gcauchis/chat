package com.gc.irc.server.service.neo4j.impl;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.entity.SerializableBufferedImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gc.irc.common.protocol.item.MessageItemPicture;
import com.gc.irc.server.service.api.IUserPictureService;
import com.gc.irc.server.service.neo4j.model.UserInformationEntity;
import com.gc.irc.server.service.neo4j.repository.UserInformationRepository;

@Service("userPictureManagement")
@Transactional
public class UserPictureService extends AbstractLoggable implements
		IUserPictureService {

	/** The repository. */
	private UserInformationRepository userInformationRepository;
	
	/**
     * Gets the user info.
     * 
     * @param id
     *            the id
     * @return the user info
     */
    private UserInformationEntity getUserInfo(final long id) {
        return userInformationRepository.findOne(id);
    }

	@Override
	public boolean newPicture(long idUser, MessageItemPicture image) {
		UserInformationEntity user = getUserInfo(idUser);
		if (user == null) {
			return false;
		}
		user.setPict(image.getImageBuffer().getByteImage());
		userInformationRepository.save(user);
		return true;
	}

	@Override
	public MessageItemPicture getPictureOf(long idUser) {
		UserInformationEntity user = getUserInfo(idUser);
		if (user == null || user.getPict() == null) {
			return null;
		}
		return new MessageItemPicture(user.getId(), new SerializableBufferedImage(user.getPict()));
	}

	@Autowired
	public void setUserInformationRepository(UserInformationRepository userInformationRepository) {
		this.userInformationRepository = userInformationRepository;
	}

}
