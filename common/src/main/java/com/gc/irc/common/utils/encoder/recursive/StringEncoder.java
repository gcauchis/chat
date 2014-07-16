package com.gc.irc.common.utils.encoder.recursive;

/**
 * The Interface IStringEncoder.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface StringEncoder {

    /**
     * Encrypt.
     *
     * @param value
     *            the value
     * @return the encrypted value
     */
    String encode(String value);
}
