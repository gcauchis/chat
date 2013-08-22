package com.gc.irc.server.service.neo4j.impl;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.server.model.UserInformations;
import com.gc.irc.server.service.api.IAuthenticationService;
import com.gc.irc.server.service.neo4j.model.UserInformationEntity;
import com.gc.irc.server.service.neo4j.model.utils.ModelConversionUtils;
import com.gc.irc.server.service.neo4j.repository.UserInformationRepository;

/**
 * The Class AuthenticationService.
 */
@Service("authenticationService")
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
    @Override
    @Transactional
    public boolean addNewUser(final String login, final String password, final String nickname) {
    	Node node = graphDatabaseService.createNode();
    	node.setProperty("__type__", "usr");
        return userInformationRepository.save(new UserInformationEntity(new UserInformations(node.getId(),nickname, login, password))) != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.service.api.IAuthenticationService#getUser(long)
     */
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
    @Override
    public UserInformations logUser(final String login, final String password) {
        return null;
    }

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
    @Override
    @Transactional
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
    @Override
    @Transactional
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
    @Override
    @Transactional
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
    @Override
    public boolean userLoginExist(final String login) {
        return userInformationRepository.findByPropertyValue("login", login) != null;
    }

}
