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
@TypeAlias("usr")
public class UserInformationEntity {
    
    /** The id. */
    @GraphId
    private Long id;

    /** The login. */
    private String log;

    /** The nickname. */
    private String nick;

    /** The password. */
    private String pwd;
    
    private byte[] pict;

    public UserInformationEntity() {
    }
    
    public UserInformationEntity(UserInformations userInformations) {
        id = userInformations.getId();
        log = userInformations.getLogin();
        nick = userInformations.getNickname();
        pwd = userInformations.getPassword();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public byte[] getPict() {
        return pict;
    }

    public void setPict(byte[] pict) {
        this.pict = pict;
    }
    
}
