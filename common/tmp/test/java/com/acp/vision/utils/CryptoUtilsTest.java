package com.acp.vision.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * The Class CryptoUtilsTest.
 */
public class CryptoUtilsTest {

    /**
     * Decrypt.
     * 
     * @param encryptedValue the encrypted value
     * @return the string
     */
    private String decrypt(final String encryptedValue) {
        final String decrytedValue = CryptoUtils.decrypt(encryptedValue);
        System.out.println("decrypt : " + encryptedValue + " --> " + decrytedValue);
        return decrytedValue;
    }

    /**
     * Encrypt.
     * 
     * @param value the value
     * @return the string
     */
    private String encrypt(final String value) {
        final String encryptedValue = CryptoUtils.encrypt(value);
        System.out.println("encrypt : " + value + " --> " + encryptedValue);
        return encryptedValue;
    }

    /**
     * Encrypt decrypt.
     */
    @Test
    public void encryptDecrypt() {
        final String value = "azerty12345&*îïù$µ¤âäèéëê";

        final String encryptedValue = encrypt(value);
        assertNotSame(value, encryptedValue);

        final String decrytedValue = decrypt(encryptedValue);
        assertEquals(value, decrytedValue);

    }

    /**
     * List.
     */
    @Test
    public void list() {

        final List < String > coll = new ArrayList < String >();
        coll.add("visionuser");
        coll.add("visionint");
        coll.add("visionrct");
        coll.add("andiamoint");
        coll.add("andiamorct");
        coll.add("visionsandbox");
        coll.add("testmagali");
        coll.add("testgael");
        coll.add("testgabriel");
        coll.add("testsam");
        coll.add("testimport");

        coll.add("cryptouser");
        coll.add("visionintcrypto");
        coll.add("andiamointcrypto");
        coll.add("cryptosandbox");

        coll.add("pcard001");
        coll.add("vision001");

        coll.add("vision");
        coll.add("vision2");
        coll.add("vision3");
        coll.add("vcrypto");

        for (final String current : coll) {
            encrypt(current);
        }

    }

}
