package com.gc.irc.server.service.neo4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.entity.SerializableBufferedImage;
import com.gc.irc.common.protocol.item.MessageItemPicture;
import com.gc.irc.server.service.neo4j.model.UserInformationEntity;
import com.gc.irc.server.service.neo4j.repository.UserInformationRepository;

/**
 * <p>UserPictureService class.</p>
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Service("userPictureManagement")
@Transactional
public class UserPictureService extends AbstractLoggable implements
		com.gc.irc.server.service.UserPictureService {

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

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	@Override
	public MessageItemPicture getPictureOf(long idUser) {
		UserInformationEntity user = getUserInfo(idUser);
		if (user == null || user.getPict() == null) {
			return null;
		}
		return new MessageItemPicture(user.getId(), new SerializableBufferedImage(user.getPict()));
	}

	/**
	 * <p>Setter for the field <code>userInformationRepository</code>.</p>
	 *
	 * @param userInformationRepository a {@link com.gc.irc.server.service.neo4j.repository.UserInformationRepository} object.
	 */
	@Autowired
	public void setUserInformationRepository(UserInformationRepository userInformationRepository) {
		this.userInformationRepository = userInformationRepository;
	}

}
