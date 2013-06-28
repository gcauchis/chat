package com.gc.irc.common.protocol.command;

/**
 * The Class IRCMessageCommandRegister.
 */
public class MessageCommandRegister extends MessageCommandLogin {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1658588296416782975L;

    /**
     * Instantiates a new iRC message command register.
     * 
     * @param login
     *            the login
     * @param password
     *            the password
     */
    public MessageCommandRegister(final String login, final String password) {
        super(login, password);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IRCMessageCommandRegister [").append(super.toString()).append(", ]");
        return builder.toString();
    }

}
