package com.gc.irc.common.abs;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

public abstract class AbstractObjectPool<T> extends AbstractLoggable {

    /** The pool. */
    private GenericObjectPool pool;

    /**
     * Free producer.
     * 
     * @param messageProducer
     *            the message producer
     */
    protected final void freePoolableObject(T messageProducer) {
        try {
            getPool().returnObject(messageProducer);
        } catch (Exception e) {
            getLog().error("Fail to return poolable Object", e);
        }
    }

    /**
     * Gets the producer.
     * 
     * @param messageProducer
     *            the message producer
     * @return the producer
     */
    @SuppressWarnings("unchecked")
    protected final T getPollableObject() {
        T messageProducer = null;
        try {
            messageProducer = (T) getPool().borrowObject();
        } catch (Exception e) {
            getLog().error("Fail to build poolable Object", e);
        }
        return messageProducer;
    }

    /**
     * Gets the pool.
     * 
     * @return the pool
     */
    private GenericObjectPool getPool() {
        if (pool == null) {
            pool = new GenericObjectPool(getPoolableObjectFactory());
        }
        return pool;
    }

    /**
     * Gets the poolable object factory.
     * 
     * @return the poolable object factory
     */
    protected abstract PoolableObjectFactory getPoolableObjectFactory();

    /**
     * Sets the max pool size.
     * 
     * @param maxPoolSize
     *            the new max pool size
     */
    public void setMaxPoolSize(int maxPoolSize) {
        getPool().setMaxActive(maxPoolSize);
    }

}
