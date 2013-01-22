package com.acp.vision.utils.test.entities;

import org.apache.commons.lang.StringUtils;

import com.acp.common.crypto.api.IStringEncoder;
import com.acp.common.crypto.exception.EncoderException;

/**
 * The Class SpecificObjectToEncodeEncoder.
 */
public class SpecificObjectToEncodeEncoder
        extends AbstractObjectToEncodeEncoder {

    /*
     * (non-Javadoc)
     * @see com.acp.vision.utils.test.entities.AbstractObjectToEncodeEncoder#spcificEncodeObject(com.acp.vision.utils.test.entities.ObjectToEncode, com.acp.common.crypto.api.IStringEncoder)
     */
    @Override
    protected ObjectToEncode spcificEncodeObject(ObjectToEncode obj, IStringEncoder stringEncoder) throws EncoderException {
        if (StringUtils.isNotEmpty(obj.getParam2())) {
            obj.setParam2(stringEncoder.encode(obj.getParam2()));
        }
        return obj;
    }

}
