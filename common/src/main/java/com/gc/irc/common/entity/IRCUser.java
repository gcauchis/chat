package com.gc.irc.common.entity;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * The Class IRCUser.
 */
public class IRCUser implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7180427709043943598L;
	
	/** The Constant logger. */
	private final static Logger logger = Logger.getLogger(IRCUser.class);
	
	/** The id. */
	private int id;
	
	/** The nick name. */
	private String nickName;
	
	/** The user status. */
	private UserStatus userStatus = UserStatus.ONLINE;
	
	/** The has pictur. */
	private boolean hasPictur = false;
	
	/**
	 * Builder.
	 * @param id User's id.
	 * @param nickName User's NickName.
	 */
	public IRCUser(int id, String nickName) {
		this.id = id;
		this.nickName = nickName;
		
		logger.debug("New User : "+id+", "+nickName+", pictur : "+hasPictur);
	}
	
	/**
	 *  Builder.
	 * @param id User's id.
	 * @param nickName User's NickName.
	 * @param hasPicture True if User has a picture.
	 */
	public IRCUser(int id, String nickName, boolean hasPicture) {
		this(id, nickName);
		this.hasPictur = hasPicture;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the nick name.
	 *
	 * @return the nick name
	 */
	public String getNickName() {
		return nickName;
	}
	
	/**
	 * Sets the nick name.
	 *
	 * @param nickName the new nick name
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	/**
	 * Checks for pictur.
	 *
	 * @return true, if successful
	 */
	public boolean hasPictur() {
		return hasPictur;
	}
	
	/**
	 * Gets the user status.
	 *
	 * @return the user status
	 */
	public UserStatus getUserStatus() {
		return userStatus;
	}
	
	/**
	 * Sets the user status.
	 *
	 * @param userStatus the new user status
	 */
	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}
	
	/**
	 * Generate xml.
	 * @param tab Indentation's level.
	 * @return Xml in a String.
	 */
	public String toStringXML(String tab) {
		String result = tab+"<IRCUser id=\""+id+"\" nickname=\""+nickName+"\" status=\""+userStatus+"\" hasPictur=\""+hasPictur+"\" />\n";
		
		return result;
	}
	
	/**
	 * Sets the checks for pictur.
	 *
	 * @param hasPictur the new checks for pictur
	 */
	public void setHasPictur(boolean hasPictur) {
		this.hasPictur = hasPictur;
	}
	
	/**
	 * Get a copy of the user.
	 * @return A copy.
	 */
	public IRCUser getCopy() {
		logger.debug("Create a copy");
		IRCUser userCopy = new IRCUser(id, nickName, hasPictur);
		userCopy.setUserStatus(userStatus);
		return userCopy;
	}

}
