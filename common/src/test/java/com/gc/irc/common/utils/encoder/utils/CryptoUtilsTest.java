package com.gc.irc.common.utils.encoder.utils;

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
        coll.add("testgabriel");

        for (final String current : coll) {
            encrypt(current);
        }

    }

}
