/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gc.irc.server.service.neo4j.model.utils;

import com.gc.irc.server.model.UserInformations;
import com.gc.irc.server.service.neo4j.model.UserInformationEntity;

/**
 * <p>ModelConversionUtils class.</p>
 *
 * @version 0.0.4
 * @author x472511
 */
public class ModelConversionUtils {
    /**
     * <p>Constructor for ModelConversionUtils.</p>
     */
    private ModelConversionUtils(){};
    
    /**
     * <p>convert.</p>
     *
     * @param userInformationEntity a {@link com.gc.irc.server.service.neo4j.model.UserInformationEntity} object.
     * @return a {@link com.gc.irc.server.model.UserInformations} object.
     */
    public static UserInformations convert(UserInformationEntity userInformationEntity) {
    	if (userInformationEntity == null) {
    		return null;
    	}
        return new UserInformations(userInformationEntity.getId(), userInformationEntity.getNick(),
                userInformationEntity.getLog(), userInformationEntity.getPwd(), userInformationEntity.getPict() != null);
    }
}
