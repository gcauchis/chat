package com.gc.irc.common.utils;

/**
 * The Class XorString.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public final class XorString {

    /**
     * Xor hex.
     *
     * @param a
     *            the a
     * @param b
     *            the b
     * @return the string
     */
    public static String xorHex(final String a, final String b) {
        final char[] chars = new char[a.length()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
        }
        return new String(chars);
    }

    /**
     * Instantiates a new xor string.
     */
    private XorString() {
        super();
    }

    /**
     * From hex.
     * 
     * @param c
     *            the c
     * @return the int
     */
    private static int fromHex(final char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        throw new IllegalArgumentException();
    }

    /**
     * To hex.
     * 
     * @param nybble
     *            the nybble
     * @return the char
     */
    private static char toHex(final int nybble) {
        if (nybble < 0 || nybble > 15) {
            throw new IllegalArgumentException();
        }
        return "0123456789ABCDEF".charAt(nybble);
    }

}
