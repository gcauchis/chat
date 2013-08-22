/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gc.irc.server.service.neo4j.model.utils;

import com.gc.irc.server.model.UserInformations;
import com.gc.irc.server.service.neo4j.model.UserInformationEntity;

/**
 *
 * @author gcauchis
 */
public class ModelConversionUtils {
    private ModelConversionUtils(){};
    
    public static UserInformations convert(UserInformationEntity userInformationEntity) {
        return new UserInformations(userInformationEntity.getId(), userInformationEntity.getNick(),
                userInformationEntity.getLog(), userInformationEntity.getPwd(), userInformationEntity.getPict() != null);
    }
}
