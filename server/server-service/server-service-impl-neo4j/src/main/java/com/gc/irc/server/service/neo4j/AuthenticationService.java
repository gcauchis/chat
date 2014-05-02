package com.gc.irc.server.service.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.server.model.UserInformations;
import com.gc.irc.server.service.IAuthenticationService;
import com.gc.irc.server.service.neo4j.model.UserInformationEntity;
import com.gc.irc.server.service.neo4j.model.utils.ModelConversionUtils;
import com.gc.irc.server.service.neo4j.repository.UserInformationRepository;

/**
 * The Class AuthenticationService.
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Service("authenticationService")
@Transactional
public class AuthenticationService extends AbstractLoggable implements IAuthenticationService {

    /** The repository. */
    private UserInformationRepository userInformationRepository;
    
    @Autowired
    GraphDatabaseService graphDatabaseService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.service.api.IAuthenticationService#addNewUser(java.
     * lang.String, java.lang.String, java.lang.String)
     */
    /** {@inheritDoc} */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean addNewUser(final String login, final String password, final String nickname) {
    	Node node = graphDatabaseService.createNode();
    	node.setProperty("__type__", "usr");
    	UserInformationEntity user = userInformationRepository.save(new UserInformationEntity(new UserInformations(node.getId(),nickname, login, password)));
    	return user != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.service.api.IAuthenticationService#getUser(long)
     */
    /** {@inheritDoc} */
    @Override
    public UserInformations getUser(final long id) {
        final UserInformationEntity userInformationEntity = getUserInfo(id);
        if (userInformationEntity != null) {
            return ModelConversionUtils.convert(userInformationEntity);
        }
        return null;
    }

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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.service.api.IAuthenticationService#logUser(java.lang
     * .String, java.lang.String)
     */
    /** {@inheritDoc} */
    @Override
    public UserInformations logUser(final String login, final String password) {
    	UserInformationEntity user = userInformationRepository.findByPropertyValue("log", login);
    	if (user == null || !user.getPwd().equals(password)) {
    		return null;
    	}
        return ModelConversionUtils.convert(user);
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

	/*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.service.api.IAuthenticationService#update(com.gc.irc
     * .server.model.UserInformations)
     */
    /** {@inheritDoc} */
    @Override
    public void update(final UserInformations userInformations) {
        final UserInformationEntity userInformationEntity = getUserInfo(userInformations.getId());
        if (userInformationEntity != null) {
            userInformationEntity.setLog(userInformations.getLogin());
            userInformationEntity.setNick(userInformations.getNickname());
            userInformationEntity.setPwd(userInformations.getPassword());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.service.api.IAuthenticationService#updateUserNickName
     * (long, java.lang.String)
     */
    /** {@inheritDoc} */
    @Override
    public void updateUserNickName(final long id, final String nickname) {
        final UserInformationEntity userInformationEntity = getUserInfo(id);
        if (userInformationEntity != null) {
            userInformationEntity.setNick(nickname);
            userInformationRepository.save(userInformationEntity);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.service.api.IAuthenticationService#updateUserPasword
     * (long, java.lang.String)
     */
    /** {@inheritDoc} */
    @Override
    public void updateUserPasword(final long id, final String password) {
        final UserInformationEntity userInformationEntity = getUserInfo(id);
        if (userInformationEntity != null) {
            userInformationEntity.setPwd(password);
            userInformationRepository.save(userInformationEntity);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.service.api.IAuthenticationService#userLoginExist(java
     * .lang.String)
     */
    /** {@inheritDoc} */
    @Override
    public boolean userLoginExist(final String login) {
        return userInformationRepository.findByPropertyValue("log", login) != null;
    }

	/** {@inheritDoc} */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void delete(long id) {
		userInformationRepository.delete(id);
	}

}
