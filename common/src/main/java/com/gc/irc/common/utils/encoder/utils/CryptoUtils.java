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
import org.slf4j.LoggerFactory;

/**
 * The Class CryptoUtils.
 * 
 */
public class CryptoUtils {

    /** The Constant LOGGER. */
    private static final transient Logger LOGGER = LoggerFactory.getLogger(CryptoUtils.class);

    /** The dcipher. */
    private static Cipher dcipher;

    /** The ecipher. */
    private static Cipher ecipher;

    /** The Constant iterationCount. */
    private static final int iterationCount = 19;

    /** The Constant keypass. */
    private static final String keypass = "GCiv01Azerty/";

    /** The Constant salt. */
    private static final byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03 };

    /**
     * {@inheritDoc}
     * 
     * @see com.acp.vision.service.IPasswordCryptoService#decrypt(java.lang.String)
     */
    public static String decrypt(final String str) {

        try {
            final KeySpec keySpec = new PBEKeySpec(keypass.toCharArray(), salt, iterationCount);
            final SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            /* Prepare the parameters to the cipthers */
            final AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

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
     * @see com.acp.vision.service.IPasswordCryptoService#encrypt(java.lang.String)
     */
    public static String encrypt(final String str) {

        try {
            final KeySpec keySpec = new PBEKeySpec(keypass.toCharArray(), salt, iterationCount);
            final SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            final AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

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
    public CryptoUtils() {
        super();
    }

}