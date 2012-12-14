package com.gc.irc.common.utils.encoder.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class HexaEncodageUtils.
 */
public final class HexaEncodageUtils {

    /**
     * Instantiates a new hexa encodage utils.
     */
    private HexaEncodageUtils() {
        super();
    }

    /**
     * Convert an Hexadecimal encoded String into a decoded byte array.
     * 
     * @param s
     *            an Hexadecimal encoded String
     * @return a decoded byte array
     */
    public static byte[] hexStringToByteArray(final String s) {
        if (StringUtils.isEmpty(s)) {
            return new byte[0];
        }
        final int len = s.length();
        final byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Hex string to string.
     * 
     * @param input
     *            the input
     * @return the string
     */
    public static String hexStringToString(final String input) {
        return new String(hexStringToByteArray(input));
    }

    /**
     * Convert a byte array into an hexadecimal encoded string.
     * 
     * @param b
     *            a byte array
     * @return hexadecimal encoded string
     * @throws Exception
     *             the exception
     */
    public static String getHexString(final byte[] b) {
        if (b == null || b.length <= 0) {
            return "";
        }
        final StringBuffer result = new StringBuffer("");
        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    /**
     * Gets the hex string.
     * 
     * @param input
     *            the input
     * @return the hex string
     */
    public static String getHexString(final String input) {
        return getHexString(input.getBytes());
    }

}
