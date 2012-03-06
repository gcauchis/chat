package com.acp.vision.service.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class HashUtils.
 */
public final class HashUtils {

    /** The LOGGER of HashUtils. */
    private static final transient Logger LOGGER = LoggerFactory.getLogger(HashUtils.class);

    /** The Constant SALT. */
    public static final String SALT = "ACP_SALT";

    /** The Constant ENCODING_HASH_TYPE. */
    public static final String ENCODING_HASH_TYPE = "SHA-512";

    /** The Constant CHARSET_NAME. */
    public static final String CHARSET_NAME = "UTF-8";

    /**
     * Instantiates a new hash utils.
     */
    private HashUtils() {
        super();
    }

    /**
     * Encode password.
     * 
     * @param input the password
     * @return the string
     */
    public static String hashSHA512(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(ENCODING_HASH_TYPE);
            byte[] data = md.digest(String.valueOf(input + SALT).getBytes(CHARSET_NAME));
            return toHexString(data);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Error Hashing SHA-512", e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error Hashing SHA-512", e);
        }
        return null;
    }

    /**
     * To hex string.
     * 
     * @param v the v
     * @return the string
     */
    private static String toHexString(byte[] v) {
        StringBuffer sb = new StringBuffer();
        byte n1, n2;

        for (int c = 0; c < v.length; c++) {
            n1 = (byte) ((v[c] & 0xF0) >>> 4);
            n2 = (byte) ((v[c] & 0x0F));

            sb.append(n1 >= 0xA ? (char) (n1 - 0xA + 'a') : (char) (n1 + '0'));
            sb.append(n2 >= 0xA ? (char) (n2 - 0xA + 'a') : (char) (n2 + '0'));
        }

        return sb.toString();
    }

}
