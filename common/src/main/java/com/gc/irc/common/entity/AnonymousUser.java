package com.gc.irc.common.entity;

/**
 * The Class AnonymousUser.
 */
public class AnonymousUser extends User {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new anonymous user.
	 *
	 * @param id the id
	 */
	public AnonymousUser(long id) {
		super(id, "Anonymous" + id);
	}

}
