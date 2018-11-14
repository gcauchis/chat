/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gc.irc.server.service.neo4j.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.gc.irc.server.model.UserInformations;

/**
 * <p>UserInformationEntity class.</p>
 *
 * @version 0.0.4
 * @author x472511
 */
@NodeEntity
@TypeAlias("usr")
public class UserInformationEntity {
    
    /** The id. */
    @GraphId
    private Long id;

    /** The login. */
    @Indexed(unique=true)
    private String log;

    /** The nickname. */
    private String nick;

    /** The password. */
    private String pwd;
    
    private byte[] pict;

    /**
     * <p>Constructor for UserInformationEntity.</p>
     */
    public UserInformationEntity() {
    }
    
    /**
     * <p>Constructor for UserInformationEntity.</p>
     *
     * @param userInformations a {@link com.gc.irc.server.model.UserInformations} object.
     */
    public UserInformationEntity(UserInformations userInformations) {
        id = userInformations.getId();
        log = userInformations.getLogin();
        nick = userInformations.getNickname();
        pwd = userInformations.getPassword();
    }

	/**
	 * <p>Getter for the field <code>id</code>.</p>
	 *
	 * @return a {@link java.lang.Long} object.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * <p>Setter for the field <code>id</code>.</p>
	 *
	 * @param id a {@link java.lang.Long} object.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * <p>Getter for the field <code>log</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getLog() {
		return log;
	}

	/**
	 * <p>Setter for the field <code>log</code>.</p>
	 *
	 * @param log a {@link java.lang.String} object.
	 */
	public void setLog(String log) {
		this.log = log;
	}

	/**
	 * <p>Getter for the field <code>nick</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * <p>Setter for the field <code>nick</code>.</p>
	 *
	 * @param nick a {@link java.lang.String} object.
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * <p>Getter for the field <code>pwd</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * <p>Setter for the field <code>pwd</code>.</p>
	 *
	 * @param pwd a {@link java.lang.String} object.
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * <p>Getter for the field <code>pict</code>.</p>
	 *
	 * @return an array of byte.
	 */
	public byte[] getPict() {
		return pict;
	}

	/**
	 * <p>Setter for the field <code>pict</code>.</p>
	 *
	 * @param pict an array of byte.
	 */
	public void setPict(byte[] pict) {
		this.pict = pict;
	}

}
