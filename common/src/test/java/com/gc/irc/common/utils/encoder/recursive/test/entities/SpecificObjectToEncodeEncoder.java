package com.gc.irc.common.utils.encoder.recursive.test.entities;

import org.apache.commons.lang3.StringUtils;

import com.gc.irc.common.exception.utils.EncoderException;
import com.gc.irc.common.utils.encoder.recursive.StringEncoder;

/**
 * The Class SpecificObjectToEncodeEncoder.
 */
public class SpecificObjectToEncodeEncoder
        extends AbstractObjectToEncodeEncoder {

    @Override
    protected ObjectToEncode spcificEncodeObject(ObjectToEncode obj, StringEncoder stringEncoder) throws EncoderException {
        if (StringUtils.isNotEmpty(obj.getParam2())) {
            obj.setParam2(stringEncoder.encode(obj.getParam2()));
        }
        return obj;
    }

}
