package com.gc.irc.server.service.neo4j.impl;

import org.springframework.stereotype.Service;

import com.gc.irc.server.model.UserInformations;
import com.gc.irc.server.service.api.IAuthenticationService;

@Service("authenticationService")
public class AuthenticationService implements IAuthenticationService {

	@Override
	public boolean addNewUser(String login, String password, String nickname) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateUserNickName(int id, String nickname) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUserPasword(int id, String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public UserInformations getUser(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserInformations logUser(String login, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(UserInformations userInformations) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean userLoginExist(String login) {
		// TODO Auto-generated method stub
		return false;
	}

}
