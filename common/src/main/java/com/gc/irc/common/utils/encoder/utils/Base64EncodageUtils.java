package com.gc.irc.common.utils.encoder.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import com.gc.irc.common.utils.LoggerUtils;

/**
 * The Class Base64EncodageUtils.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public final class Base64EncodageUtils {

    private static final String ENCODAGE_UTF_8 = "UTF-8";
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerUtils.getLogger(Base64EncodageUtils.class);

    /**
     * Instantiates a new base64 encodage utils.
     */
    private Base64EncodageUtils() {
        super();
    }

    /**
     * Checks if is base64.
     *
     * @param value
     *            the value
     * @return a boolean.
     */
    public static boolean isBase64(final String value) {
        return Base64.isBase64(value);
    }

    /**
     * Compress and encode base64.
     *
     * @param value
     *            the value
     * @return the string
     */
    public static String compressAndEncodeBase64(final String value) {
        try {
            return encodeBase64(compress(value));
        } catch (final UnsupportedEncodingException e) {
            LOGGER.warn("Fail to compress and encode value: {}", e.getMessage());
            return value;
        }
    }

    /**
     * Compress.
     *
     * @param value
     *            the value
     * @return the byte[]
     * @throws java.io.UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    public static byte[] compress(final String value) throws UnsupportedEncodingException {
        final byte[] input = value.getBytes(ENCODAGE_UTF_8);
        final byte[] output = new byte[value.length()];
        final Deflater compresser = new Deflater();
        compresser.setInput(input);
        compresser.finish();
        final int compressedDataLength = compresser.deflate(output);
        return Arrays.copyOf(output, compressedDataLength);
    }

    /**
     * Encode base64.
     *
     * @param value
     *            the value
     * @return the string
     * @throws java.io.UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    public static String encodeBase64(final byte[] value) throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(value), ENCODAGE_UTF_8);
    }

    /**
     * Encode base64.
     *
     * @param value
     *            the value
     * @return the string
     * @throws java.io.UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    public static String encodeBase64(final String value) throws UnsupportedEncodingException {
        return encodeBase64(value.getBytes(ENCODAGE_UTF_8));
    }

    /**
     * Decode base64 and decompress.
     *
     * @param value
     *            the value
     * @return the string
     */
    public static String decodeBase64AndDecompress(final String value) {
        try {
            return decompress(decodeBase64(value));
        } catch (final UnsupportedEncodingException e) {
            LOGGER.warn("Fail to decodeBase64AndDecompress value: {}", e.getMessage());
            return value;
        }
    }

    /**
     * Decode base64.
     *
     * @param value
     *            the value
     * @return the byte[]
     * @throws java.io.UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    public static byte[] decodeBase64(final String value) throws UnsupportedEncodingException {
        if (Base64.isBase64(value)) {
            return decodeBase64(value.getBytes(ENCODAGE_UTF_8));
        }
        return value.getBytes(ENCODAGE_UTF_8);
    }

    /**
     * Decode base64.
     *
     * @param value
     *            the value
     * @return the byte[]
     * @throws java.io.UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    public static byte[] decodeBase64(final byte[] value) throws UnsupportedEncodingException {
        return Base64.decodeBase64(value);
    }

    /**
     * Decompress.
     *
     * @param value
     *            the value
     * @return the string
     */
    public static String decompress(final byte[] value) {
        String resultValue = null;
        try {
            resultValue = new String(value, ENCODAGE_UTF_8);
        } catch (final UnsupportedEncodingException e) {
            LOGGER.warn("fail to build UTF-8 string", e);
        }
        try {
            final Inflater decompresser = new Inflater();
            decompresser.setInput(value, 0, value.length);
            final byte[] result = new byte[value.length * 4];
            final int resultLength = decompresser.inflate(result);
            decompresser.end();

            resultValue = new String(result, 0, resultLength, ENCODAGE_UTF_8);
        } catch (UnsupportedEncodingException | DataFormatException e) {
            LOGGER.warn("fail to decodeBase64AndDecompress: {}", e.getMessage());
        }
        return resultValue;

    }
}
