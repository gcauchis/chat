package com.gc.irc.common.protocol.command;

import com.gc.irc.common.entity.User;

/**
 * The Class IRCMessageCommandChangeNickname.
 *
 * @version 0.0.4
 * @author x472511
 */
public class MessageCommandChangeNickname extends MessageCommand {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4197111507731979380L;

    /** The nickname. */
    private String nickname;

    /**
     * Instantiates a new iRC message command change nickname.
     *
     * @param user
     *            the user
     */
    public MessageCommandChangeNickname(final User user) {
        super(user.getId());
        nickname = user.getNickName();
    }

    /**
     * Gets the new nickname.
     *
     * @return the new nickname
     */
    public String getNewNickname() {
        return nickname;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("IRCMessageCommandChangeNickname [").append(super.toString()).append(", nickname=").append(nickname).append("]");
        return builder.toString();
    }

}
