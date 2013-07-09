package com.gc.common.utils.test.entities;

import org.apache.commons.lang.StringUtils;

import com.acp.common.crypto.api.IStringEncoder;
import com.acp.common.crypto.exception.EncoderException;
import com.gc.common.encoder.AbstractObjectEncoder;

/**
 * The Class AbstractObjectToEncodeEncoder.
 */
public abstract class AbstractObjectToEncodeEncoder
        extends AbstractObjectEncoder < ObjectToEncode > {

    /*
     * (non-Javadoc)
     * @see com.gc.common.encoder.AbstractObjectEncoder#internalEncodeObject(java.lang.Object, com.acp.common.crypto.api.IStringEncoder)
     */
    @Override
    protected ObjectToEncode internalEncodeObject(ObjectToEncode obj, IStringEncoder stringEncoder) throws EncoderException {
        if (StringUtils.isNotEmpty(obj.getParam1())) {
            obj.setParam1(stringEncoder.encode(obj.getParam1()));
        }
        return spcificEncodeObject(obj, stringEncoder);
    }

    protected abstract ObjectToEncode spcificEncodeObject(ObjectToEncode obj, IStringEncoder stringEncoder) throws EncoderException;

}
