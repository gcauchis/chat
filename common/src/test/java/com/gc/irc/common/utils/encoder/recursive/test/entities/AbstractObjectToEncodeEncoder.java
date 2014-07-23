package com.gc.irc.common.utils.encoder.recursive.test.entities;

import org.apache.commons.lang3.StringUtils;

import com.gc.irc.common.exception.utils.EncoderException;
import com.gc.irc.common.utils.encoder.recursive.AbstractObjectEncoder;
import com.gc.irc.common.utils.encoder.recursive.StringEncoder;


/**
 * The Class AbstractObjectToEncodeEncoder.
 */
public abstract class AbstractObjectToEncodeEncoder
        extends AbstractObjectEncoder < ObjectToEncode > {

    @Override
    protected ObjectToEncode internalEncodeObject(ObjectToEncode obj, StringEncoder stringEncoder) throws EncoderException {
        if (StringUtils.isNotEmpty(obj.getParam1())) {
            obj.setParam1(stringEncoder.encode(obj.getParam1()));
        }
        return spcificEncodeObject(obj, stringEncoder);
    }

    protected abstract ObjectToEncode spcificEncodeObject(ObjectToEncode obj, StringEncoder stringEncoder) throws EncoderException;

}
