package com.gc.irc.server.service.neo4j.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.server.model.UserInformations;
import com.gc.irc.server.service.api.IAuthenticationService;
import com.gc.irc.server.service.neo4j.model.UserInformationEntity;
import com.gc.irc.server.service.neo4j.model.utils.ModelConversionUtils;

/**
 * The Class AuthenticationService.
 */
@Service("authenticationService")
@Transactional
public class AuthenticationService extends AbstractLoggable implements IAuthenticationService {

    /** The repository. */
    private GraphRepository<UserInformationEntity> repository;

    /** The template. */
    private Neo4jTemplate template;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.service.api.IAuthenticationService#addNewUser(java.
     * lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean addNewUser(final String login, final String password, final String nickname) {
        return repository.save(new UserInformationEntity(new UserInformations(nickname, login, password))) != null;
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
        return repository.findOne(id);
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

    /**
     * Sets the template.
     * 
     * @param template
     *            the new template
     */
    @Autowired
    public void setTemplate(final Neo4jTemplate template) {
        this.template = template;
        repository = template.repositoryFor(UserInformationEntity.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.service.api.IAuthenticationService#update(com.gc.irc
     * .server.model.UserInformations)
     */
    @Override
    public void update(final UserInformations userInformations) {
        final UserInformationEntity userInformationEntity = getUserInfo(userInformations.getId());
        if (userInformationEntity != null) {
            userInformationEntity.setLogin(userInformations.getLogin());
            userInformationEntity.setNickname(userInformations.getNickname());
            userInformationEntity.setPassword(userInformations.getPassword());
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
    public void updateUserNickName(final long id, final String nickname) {
        final UserInformationEntity userInformationEntity = getUserInfo(id);
        if (userInformationEntity != null) {
            userInformationEntity.setNickname(nickname);
            repository.save(userInformationEntity);
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
    public void updateUserPasword(final long id, final String password) {
        final UserInformationEntity userInformationEntity = getUserInfo(id);
        if (userInformationEntity != null) {
            userInformationEntity.setPassword(password);
            repository.save(userInformationEntity);
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
        return repository.findByPropertyValue("login", login) != null;
    }

}
