package com.gc.irc.common.protocol.command;

/**
 * The Class IRCMessageCommandRegister.
 */
public class IRCMessageCommandRegister extends IRCMessageCommand {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1658588296416782975L;

	/** The login. */
	private String login;

	/** The password. */
	private String password;

	/**
	 * Instantiates a new iRC message command register.
	 * 
	 * @param login
	 *            the login
	 * @param password
	 *            the password
	 */
	public IRCMessageCommandRegister(final String login, final String password) {
		super(-1, IRCMessageCommandType.REGISTER);
		this.login = login;
		this.password = password;
	}

	/**
	 * Gets the login.
	 * 
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("IRCMessageCommandRegister [login=").append(login)
				.append(", password=").append(password).append("]");
		return builder.toString();
	}

}
