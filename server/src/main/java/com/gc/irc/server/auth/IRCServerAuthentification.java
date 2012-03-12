package com.gc.irc.server.auth;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.item.IRCMessageItemPicture;
import com.gc.irc.common.utils.IOStreamUtils;
import com.gc.irc.common.utils.IOUtils;
import com.gc.irc.server.persistance.IRCGestionPicture;

/**
 * Singleton class use for login and register all the users.
 * 
 * @author gcauchis
 * 
 */
public class IRCServerAuthentification implements AuthentificationInterface {
	private static final Logger logger = Logger
			.getLogger(IRCServerAuthentification.class);
	private static IRCServerAuthentification instance;
	private List<IRCUserInformations> listUsers;
	private String pathFichier = "auth.xml";
	private static int lastId;

	private static void setLastId(int lastId) {
		IRCServerAuthentification.lastId = lastId;
	}

	private int getNewId() {
		lastId++;
		return lastId;
	}

	/**
	 * Read the Users data.
	 */
	private IRCServerAuthentification() {
		logger.debug("Read the Users data.");
		try {
			new UserImformationScanner(pathFichier);
		} catch (ParserConfigurationException e) {
			logger.warn("Fail to parse xml.");
			e.printStackTrace();
		} catch (SAXException e) {
			logger.warn("Fail to parse xml.");
			e.printStackTrace();
		} catch (IOException e) {
			logger.warn("Fail to read xml file." + e.getMessage());
			logger.info("If file didn't exist yet, don't worry with this error");
		}
		setLastId(UserImformationScanner.getLastId());
		listUsers = UserImformationScanner.getListUserInfomation();
		logger.debug("End init auth.");
	}

	/**
	 * Get an instance of the class.
	 * 
	 * @return The unique instance of the Class.
	 */
	public synchronized static IRCServerAuthentification getInstance() {
		if (instance == null) {
			instance = new IRCServerAuthentification();
		}
		return instance;
	}

	public boolean addUser(String login, String password, String nickname) {
		if (userLoginExist(login)) {
			logger.info("Login already exist");
			return false;
		}
		listUsers.add(new IRCUserInformations(getNewId(), nickname, login,
				password));
		saveModification();
		return true;
	}

	public boolean userLoginExist(String login) {
		for (IRCUserInformations user : listUsers) {
			if (user.loginEquals(login)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Generate xml code.
	 * 
	 * @return xml code in a String.
	 */
	private String generateXML() {
		String result = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\" ?>\n\n";
		result += "<IRCUsers lastId=\"" + lastId + "\">\n";
		for (IRCUserInformations user : listUsers) {
			result += user.toStringXML("\t");
		}

		result += "</IRCUsers>\n";

		return result;
	}

	/**
	 * Persist the modification.
	 */
	public void saveModification() {
		synchronized (listUsers) {
			IOUtils.ecritFichier(pathFichier, generateXML());
		}
	}

	public IRCUser logUser(String login, String password) {

		for (IRCUserInformations user : listUsers) {
			if (user.getLogin().equals(login)
					&& user.getPassword().equals(password)) {
				if (user.isConnecte()) {
					return null;
				}
				return user.getUser();
			}
		}
		return null;
	}

	public void changeNickUser(int id, String nickname) {
		for (IRCUserInformations user : listUsers) {
			if (user.getId() == id) {
				user.setNickname(nickname);
				return;
			}
		}
	}

	public void changePasswordUser(int id, String password) {
		for (IRCUserInformations user : listUsers) {
			if (user.getId() == id) {
				user.setPassword(password);
				return;
			}
		}
	}

	public IRCUserInformations getUser(int id) {
		for (IRCUserInformations user : listUsers) {
			if (user.getId() == id) {
				return user;
			}
		}
		return null;
	}

	public synchronized void sendUsersPicture(ObjectOutputStream outObject) {
		IRCMessageItemPicture messagePicture;
		for (IRCUserInformations user : listUsers) {
			if (user.isConnecte() && user.hasPictur()) {
				IRCGestionPicture picturUser = IRCGestionPicture.getInstance();
				messagePicture = picturUser.getPictureOf(user.getId());
				if (messagePicture != null) {
					try {
						IOStreamUtils.sendMessage(outObject, messagePicture);
					} catch (IOException e) {
						logger.warn("Fail to send the picture of "
								+ user.getNickname() + " : " + e.getMessage());
						e.printStackTrace();
					}
				} else {
					logger.warn("Fail to open the picture of "
							+ user.getNickname());
				}
			}
		}
	}
}
