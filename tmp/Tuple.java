package com.acp.acs.common.utils;

/**
 * Class representing a tuple with 2 elements.
 * 
 * @param <K>
 *            First Element type
 * @param <V>
 *            Second Element type
 */
public final class Tuple < K, V > {

    /** First element. */
    private final K first;

    /** Second element. */
    private final V last;

    /**
     * Tuple constructor.
     * 
     * @param first
     *            First element.
     * @param last
     *            Second element.
     */
    public Tuple(final K first, final V last) {
        this.first = first;
        this.last = last;
    }

    /**
     * Getter for first element.
     * 
     * @return the first
     */
    public K first() {
        return first;
    }

    /**
     * Getter for second element.
     * 
     * @return the last
     */
    public V last() {
        return last;
    }

}
