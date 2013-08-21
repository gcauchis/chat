package com.gc.irc.server.service.neo4j.impl;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.gc.irc.common.protocol.item.MessageItemPicture;
import com.gc.irc.server.service.api.IUserPictureService;

@Service("userPictureManagement")
public class UserPictureService implements IUserPictureService {

	@Override
	public Logger getLog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean newPicture(int idUser, MessageItemPicture image) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MessageItemPicture getPictureOf(int idUser) {
		// TODO Auto-generated method stub
		return null;
	}

}
