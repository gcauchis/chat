package com.acp.acs.common.utils;

/**
 * The Class CardNumberUtils.
 */
public final class PANUtils {

    /**
     * Instantiates a new card number utils.
     */
    private PANUtils() {
        super();
    }

    /**
     * Builds the error pan.
     * 
     * @param size
     *            the size
     * @return the string
     */
    public static String buildErrorPan(final int size) {
        final StringBuilder pan = new StringBuilder();
        for (int i = 0; i < size; i++) {
            pan.append("0");
        }
        return pan.toString();
    }

}
