package com.acp.vision.service.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * The Class HashUtilsTest.
 */
public class HashUtilsTest {

    /**
     * Hash sh a512.
     */
    @Test
    public void hashSHA512() {
        assertNotNull(makeAndDisplayHash("ADMIN"));
        assertEquals(makeAndDisplayHash("ADMIN"), makeAndDisplayHash("ADMIN"));
        assertNotNull(makeAndDisplayHash("123"));
        assertNotNull(makeAndDisplayHash("1234"));
        assertNotNull(makeAndDisplayHash("12345"));
        assertNotNull(makeAndDisplayHash("123456"));
        assertNotNull(makeAndDisplayHash("654321"));
        assertNotNull(makeAndDisplayHash("00096643"));
    }

    /**
     * Make and display hash.
     * 
     * @param message the message
     * @return the string
     */
    private String makeAndDisplayHash(final String message) {
        final String hashSHA512 = HashUtils.hashSHA512(message);
        System.out.println(message + " => " + hashSHA512);
        return hashSHA512;
    }

}
