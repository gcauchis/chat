package com.gc.irc.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class IOUtils.
 */
public final class IOUtils {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

    /**
     * Instantiates a new iO utils.
     */
    private IOUtils() {
        super();
    }

    /**
     * Byte to char.
     * 
     * @param buffer
     *            the buffer
     * @param size
     *            the size
     * @return the char[]
     */
    public static char[] byteToChar(final byte[] buffer, int size) {
        if (size > buffer.length) {
            size = buffer.length;
        }
        final char[] bufferChar = new char[size];

        for (int i = 0; i < size; i++) {
            bufferChar[i] = (char) buffer[i];
        }

        return bufferChar;
    }

    /**
     * Byte to string.
     * 
     * @param buffer
     *            the buffer
     * @param size
     *            the size
     * @return the string
     */
    public static String byteToString(final byte[] buffer, int size) {
        if (size > buffer.length) {
            size = buffer.length;
        }
        return String.valueOf(byteToChar(buffer, size), 0, size);
    }

    /**
     * Function wich converts a text file into a String Can be useful to read a
     * configuration file for example.
     * 
     * @param f
     *            The file we want to read
     * @return A string containing all the content of the file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static String loadFile(final File f) throws IOException {
        final BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
        final StringWriter out = new StringWriter();
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        out.flush();
        out.close();
        in.close();
        return out.toString();
    }

    /**
     * Write file.
     * 
     * @param filename
     *            the filename
     * @param content
     *            the content
     */
    public static void writeFile(final String filename, final String content) {
        final File file = new File(filename);

        PrintStream lPrintStream = null;
        FileOutputStream lFos = null;
        try {
            lFos = new FileOutputStream(file, false);
            lPrintStream = new PrintStream(new BufferedOutputStream(lFos));
            lPrintStream.println(content);
        } catch (final Exception e) {
            LOGGER.error("Fail to write file.", e);
        } finally {
            try {
                if (lPrintStream != null) {
                    lPrintStream.flush();
                    lPrintStream.close();
                }
                if (lFos != null) {
                    lFos.close();
                }
            } catch (final Exception e) {
                LOGGER.error("Fail to close file.", e);
            }
        }
    }
}
