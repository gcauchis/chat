package com.gc.irc.common.utils.encoder.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import com.gc.irc.common.utils.LoggerUtils;

/**
 * The Class CryptoUtils.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public final class CryptoUtils {

    /** The Constant LOGGER. */
    private static final transient Logger LOGGER = LoggerUtils.getLogger(CryptoUtils.class);

    /** The dcipher. */
    private static Cipher dcipher;

    /** The ecipher. */
    private static Cipher ecipher;

    /** The Constant iterationCount. */
    private static final int ITERATION_COUNT = 19;

    /** The Constant keypass. */
    private static final String KEY_PASS = "GCiv01Azerty/";

    /** The Constant salt. */
    private static final byte[] SALT = { (byte) 0x5D, (byte) 0x59, (byte) 0xA7, (byte) 0x0F, (byte) 0x96, (byte) 0xE6, (byte) 0xB4, (byte) 0x6D };

    /**
     * {@inheritDoc}
     *
     * @see com.gc.common.service.IPasswordCryptoService#decrypt(java.lang.String)
     * @param str a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String decrypt(final String str) {

        try {
            final KeySpec keySpec = new PBEKeySpec(KEY_PASS.toCharArray(), SALT, ITERATION_COUNT);
            final SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            /* Prepare the parameters to the cipthers */
            final AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

        } catch (final InvalidAlgorithmParameterException e) {
            LOGGER.error("EXCEPTION: InvalidAlgorithmParameterException");
        } catch (final InvalidKeySpecException e) {
            LOGGER.error("EXCEPTION: InvalidKeySpecException");
        } catch (final NoSuchPaddingException e) {
            LOGGER.error("EXCEPTION: NoSuchPaddingException");
        } catch (final NoSuchAlgorithmException e) {
            LOGGER.error("EXCEPTION: NoSuchAlgorithmException");
        } catch (final InvalidKeyException e) {
            LOGGER.error("EXCEPTION: InvalidKeyException");
        }

        try {
            // Decode base64 to get bytes
            final byte[] dec = Base64.decodeBase64(str.getBytes("UTF-8"));
            // Decode using utf-8
            return new String(dcipher.doFinal(dec), "UTF8");

        } catch (final BadPaddingException e) {
        } catch (final IllegalBlockSizeException e) {
        } catch (final UnsupportedEncodingException e) {
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see com.gc.common.service.IPasswordCryptoService#encrypt(java.lang.String)
     * @param str a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String encrypt(final String str) {

        try {
            final KeySpec keySpec = new PBEKeySpec(KEY_PASS.toCharArray(), SALT, ITERATION_COUNT);
            final SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            final AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

        } catch (final InvalidAlgorithmParameterException e) {
            LOGGER.error("EXCEPTION: InvalidAlgorithmParameterException");
        } catch (final InvalidKeySpecException e) {
            LOGGER.error("EXCEPTION: InvalidKeySpecException");
        } catch (final NoSuchPaddingException e) {
            LOGGER.error("EXCEPTION: NoSuchPaddingException");
        } catch (final NoSuchAlgorithmException e) {
            LOGGER.error("EXCEPTION: NoSuchAlgorithmException");
        } catch (final InvalidKeyException e) {
            LOGGER.error("EXCEPTION: InvalidKeyException");
        }

        try {
            /* Encrypt */
            final byte[] enc = ecipher.doFinal(str.getBytes("UTF8"));

            /* Encode bytes to base64 to get a string */
            return new String(Base64.encodeBase64(enc), "UTF-8");

        } catch (final BadPaddingException e) {
        } catch (final IllegalBlockSizeException e) {
        } catch (final UnsupportedEncodingException e) {
        }
        return null;
    }

    /**
     * Instantiates a new password crypto service.
     */
    private CryptoUtils() {
        super();
    }

}
