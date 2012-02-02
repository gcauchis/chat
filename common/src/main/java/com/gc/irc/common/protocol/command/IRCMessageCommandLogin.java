package com.gc.irc.common.protocol.command;

/**
 * The Class IRCMessageCommandLogin.
 */
public class IRCMessageCommandLogin extends IRCMessageCommand {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2201862402731895159L;

	/** The login. */
	private String login;

	/** The password. */
	private String password;

	/**
	 * Instantiates a new iRC message command login.
	 * 
	 * @param login
	 *            the login
	 * @param password
	 *            the password
	 */
	public IRCMessageCommandLogin(final String login, final String password) {
		super(-1, IRCMessageCommandType.LOGIN);
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
		builder.append("IRCMessageCommandLogin [login=").append(login)
				.append(", password=").append(password).append("]");
		return builder.toString();
	}

}
