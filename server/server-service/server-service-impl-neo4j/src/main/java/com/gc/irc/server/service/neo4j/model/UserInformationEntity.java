/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gc.irc.server.service.neo4j.model;

import com.gc.irc.server.model.UserInformations;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 *
 * @author gcauchis
 */
@NodeEntity
@TypeAlias("UserInformation")
public class UserInformationEntity {
    
    /** The id. */
    @GraphId
    private Long id;

    /** The login. */
    private String login;

    /** The nickname. */
    private String nickname;

    /** The password. */
    private String password;
    
    private byte[] picture;

    public UserInformationEntity() {
    }
    
    public UserInformationEntity(UserInformations userInformations) {
        id = userInformations.getId();
        login = userInformations.getLogin();
        nickname = userInformations.getNickname();
        password = userInformations.getPassword();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
    
}
