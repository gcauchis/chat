package com.gc.irc.common;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * The Class AbstractObjectPool.
 *
 * @param <T>
 *            the generic type
 * @version 0.0.4
 * @author x472511
 */
public abstract class AbstractObjectPool<T> extends AbstractLoggable {

    /** The pool. */
    private GenericObjectPool<T> pool;

    /**
     * Free pooled object.
     *
     * @param pooledObject
     *            the message producer
     */
    protected final void freePooledObject(final T pooledObject) {
        try {
            getPool().returnObject(pooledObject);
        } catch (final Exception e) {
            getLog().error("Fail to return poolable Object", e);
        }
    }

    /**
     * Gets the pool.
     *
     * @return the pool
     */
    private GenericObjectPool<T> getPool() {
        if (pool == null) {
            pool = new GenericObjectPool<T>(getPoolableObjectFactory());
        }
        return pool;
    }

    /**
     * Gets the poolable object factory.
     *
     * @return the poolable object factory
     */
    protected abstract PoolableObjectFactory<T> getPoolableObjectFactory();

    /**
     * Gets the pooled object.
     *
     * @return the pooled object
     */
    protected final T getPooledObject() {
        T pooledObject = null;
        try {
            pooledObject = getPool().borrowObject();
        } catch (final Exception e) {
            getLog().error("Fail to build poolable Object", e);
        }
        return pooledObject;
    }

    /**
     * Sets the max pool size.
     *
     * @param maxPoolSize
     *            the new max pool size
     */
    public void setMaxPoolSize(final int maxPoolSize) {
        getPool().setMaxActive(maxPoolSize);
    }

}
