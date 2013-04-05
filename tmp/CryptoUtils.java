package com.gc.common.security.crypto.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.common.exception.SecurityException;

/**
 * The Class CryptoUtils.
 */
public final class CryptoUtils {

    /** The Constant TRIPLE_DES_ALGORITHM. */
    private static final String TRIPLE_DES_ALGORITHM                    = "DESede";

    /** The Constant DES_ALGORITHM. */
    private static final String DES_ALGORITHM                           = "DES";

    /** The Constant BOUNCY_CASTLE_PROVIDER. */
    private static final String BOUNCY_CASTLE_PROVIDER                  = "BC";

    /** The Constant RSA_ALGO. */
    private static final String RSA_TRANSFORMATION_ALGORITHM            = "RSA/None/PKCS1Padding";

    /** The Constant DES_ALGO. */
    private static final String DES_TRANSFORMATION_ALGORITHM_NO_PADDING = "DES/ECB/NoPadding";

    /** The Constant TRIPLE_DES_ALGORITHM_NO_PADDING. */
    private static final String TRIPLE_DES_TRANSFORMATION_ALGORITHM     = "DESede/ECB/PKCS5Padding";

    /** The Constant DES_ALGORITHM. */
    private static final String DES_TRANSFORMATION_ALGORITHM            = "DES";

    /** The Constant LOGGER. */
    private static final Logger LOGGER                                  = LoggerFactory.getLogger(CryptoUtils.class);

    /**
     * Instantiates a new crypto utils.
     */
    private CryptoUtils() {
        super();
    }

    /**
     * Decrypt data asymmetric.
     * 
     * @param encryptedData
     *            the encrypted data
     * @param privateKey
     *            the private key
     * @return the byte[]
     */
    public static byte[] decryptDataAsymmetric(final byte[] encryptedData, final PrivateKey privateKey) {
        if (encryptedData == null || encryptedData.length <= 0) {
            return new byte[0];
        }
        try {
            Security.addProvider(new BouncyCastleProvider());
            final Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION_ALGORITHM, BOUNCY_CASTLE_PROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            final byte[] plainText = cipher.doFinal(encryptedData);
            return plainText;
        } catch (final Exception e) {
            LOGGER.warn("Fail to decrypte Data Asymmetric.", e);
            return encryptedData;
        }
    }

    /**
     * Encrypt data asymmetric.
     * 
     * @param data
     *            the data
     * @param publicKey
     *            the public key
     * @return the byte[]
     */
    public static byte[] encryptDataAsymmetric(final byte[] data, final PublicKey publicKey) {
        if (data == null || data.length <= 0) {
            return new byte[0];
        }
        Cipher cipher;
        try {
            Security.addProvider(new BouncyCastleProvider());
            cipher = Cipher.getInstance(RSA_TRANSFORMATION_ALGORITHM, BOUNCY_CASTLE_PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (final ArrayIndexOutOfBoundsException e) {
            LOGGER.info("ArrayIndexOutOfBoundsException: failed to encrypt value, maybe the value is already encrypted");
            return data;
        } catch (final Exception e) {
            LOGGER.warn("Fail to encrypt Data Asymmetric.", e);
            return new byte[0];
        }
    }

    /**
     * Encrypt des.
     * 
     * @param data
     *            the data
     * @param key
     *            the key
     * @return the byte[]
     */
    public static byte[] encryptDES(final byte[] data, final SecretKey key) {
        return encryptDES(data, key, true);
    }

    /**
     * Decrypt des.
     * 
     * @param data
     *            the data
     * @param key
     *            the key
     * @return the byte[]
     */
    public static byte[] decryptDES(final byte[] data, final SecretKey key) {
        return decryptDES(data, key, true);
    }

    /**
     * Encrypt des.
     * 
     * @param data
     *            the data
     * @param key
     *            the key
     * @param padding
     *            the padding
     * @return the byte[]
     */
    public static byte[] encryptDES(final byte[] data, final SecretKey key, final boolean padding) {
        return encryptSym(data, key, padding ? DES_TRANSFORMATION_ALGORITHM : DES_TRANSFORMATION_ALGORITHM_NO_PADDING);
    }

    /**
     * Encrypt triple des.
     * 
     * @param data
     *            the data
     * @param key
     *            the key
     * @return the byte[]
     */
    public static byte[] encryptTripleDES(final byte[] data, final SecretKey key) {
        return encryptSym(data, key, TRIPLE_DES_TRANSFORMATION_ALGORITHM);
    }

    /**
     * Encrypt sym.
     * 
     * @param data
     *            the data
     * @param key
     *            the key
     * @param transformationAlgorithm
     *            the transformation algorithm
     * @return the byte[]
     */
    public static byte[] encryptSym(final byte[] data, final SecretKey key, final String transformationAlgorithm) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            final Cipher encrypter = Cipher.getInstance(transformationAlgorithm, BOUNCY_CASTLE_PROVIDER);
            encrypter.init(Cipher.ENCRYPT_MODE, key);
            return encrypter.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException e) {
            LOGGER.warn("Fail to retreive DES algo", e);
        } catch (final InvalidKeyException e) {
            LOGGER.warn("Key invalid", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            LOGGER.warn("Fail to encrypt", e);
        }
        return new byte[0];
    }

    /**
     * Decrypt des.
     * 
     * @param data
     *            the data
     * @param key
     *            the key
     * @param padding
     *            the padding
     * @return the byte[]
     */
    public static byte[] decryptDES(final byte[] data, final SecretKey key, final boolean padding) {
        return decryptSym(data, key, padding ? DES_TRANSFORMATION_ALGORITHM : DES_TRANSFORMATION_ALGORITHM_NO_PADDING);
    }

    /**
     * Decrypt triple des.
     * 
     * @param data
     *            the data
     * @param key
     *            the key
     * @return the byte[]
     */
    public static byte[] decryptTripleDES(final byte[] data, final SecretKey key) {
        return decryptSym(data, key, TRIPLE_DES_TRANSFORMATION_ALGORITHM);
    }

    /**
     * Decrypt sym.
     * 
     * @param data
     *            the data
     * @param key
     *            the key
     * @param transformationAlgorithm
     *            the transformation algorithm
     * @return the byte[]
     */
    public static byte[] decryptSym(final byte[] data, final SecretKey key, final String transformationAlgorithm) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            final Cipher encrypter = Cipher.getInstance(transformationAlgorithm, BOUNCY_CASTLE_PROVIDER);
            encrypter.init(Cipher.DECRYPT_MODE, key);
            return encrypter.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException e) {
            LOGGER.warn("Fail to retreive DES algo", e);
        } catch (final InvalidKeyException e) {
            LOGGER.warn("Key invalid", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            LOGGER.warn("Fail to decrypt", e);
        }
        return data;
    }

    /**
     * Builds the des secret key.
     * 
     * @param key
     *            the key
     * @return the secret key
     * @throws SecurityException
     *             the security exception
     */
    public static SecretKey buildDESSecretKey(final byte[] key) throws SecurityException {
        return buildSecretKey(key, DES_ALGORITHM);
    }

    /**
     * Builds the triple des secret key.
     * 
     * @param key
     *            the key
     * @return the secret key
     * @throws SecurityException
     *             the security exception
     */
    public static SecretKey buildTripleDESSecretKey(final byte[] key) throws SecurityException {
        return buildSecretKey(key, TRIPLE_DES_ALGORITHM);
    }

    /**
     * Builds the secret key.
     * 
     * @param key
     *            the key
     * @param algorithm
     *            the algorithm
     * @return the secret key
     * @throws SecurityException
     *             the security exception
     */
    private static SecretKey buildSecretKey(final byte[] key, final String algorithm) throws SecurityException {
        SecretKeyFactory secretKeyFactory = null;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
        } catch (final NoSuchAlgorithmException e) {
            throw new SecurityException("Fail to buid secret key factory", e);
        }
        KeySpec keySpec = null;
        try {
            if (DES_ALGORITHM.equals(algorithm)) {
                keySpec = new DESKeySpec(key);
            } else if (TRIPLE_DES_ALGORITHM.equals(algorithm)) {
                keySpec = new DESedeKeySpec(key);
            } else {
                throw new SecurityException("Unknown Symetric algorithm");
            }
        } catch (final InvalidKeyException e) {
            throw new SecurityException("Fail to buid key spec", e);
        }
        try {
            return secretKeyFactory.generateSecret(keySpec);
        } catch (final InvalidKeySpecException e) {
            throw new SecurityException("Fail to buid spec", e);
        }
    }

}
