package com.gc.irc.server.service.neo4j.impl;

import com.gc.irc.common.abs.AbstractLoggable;
import org.springframework.stereotype.Service;

import com.gc.irc.common.protocol.item.MessageItemPicture;
import com.gc.irc.server.service.api.IUserPictureService;

@Service("userPictureManagement")
public class UserPictureService extends AbstractLoggable implements IUserPictureService {

	@Override
	public boolean newPicture(long idUser, MessageItemPicture image) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MessageItemPicture getPictureOf(long idUser) {
		// TODO Auto-generated method stub
		return null;
	}

}
