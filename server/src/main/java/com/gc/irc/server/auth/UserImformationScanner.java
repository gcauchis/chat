package com.gc.irc.server.auth;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A scanner to parse an identification xml file.
 * @author gcauchis
 *
 */
public class UserImformationScanner {
	
	/** The m document. */
	org.w3c.dom.Document mDocument;
//	private File mDirectory;
	/** The list users. */
private static List<IRCUserInformations> listUsers = Collections.synchronizedList(new ArrayList<IRCUserInformations>());
	
	/** The last id. */
	private static int lastId = 0;

	/**
	 * Instantiates a new user imformation scanner.
	 *
	 * @param document the document
	 */
	public UserImformationScanner(org.w3c.dom.Document document)
	{
		this.mDocument = document;
		listUsers = Collections.synchronizedList(new ArrayList<IRCUserInformations>());
		visitDocument();
	}

	/**
	 * Instantiates a new user imformation scanner.
	 *
	 * @param file the file
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the sAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public UserImformationScanner(String file) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		mDocument = builder.parse(new InputSource(new File(file).toURI().toString()));
		listUsers = Collections.synchronizedList(new ArrayList<IRCUserInformations>());
		visitDocument();
	}
	
	/**
	 * Scan through org.w3c.dom.Document mDocument.
	 */
	public void visitDocument()
	{
		org.w3c.dom.Element element = mDocument.getDocumentElement();
		if ((element != null) && element.getTagName().equals("IRCUsers"))
		{
			visitElement_IRCUsers(element);
		}
	}
	
	/**
	 * Visit element_ irc users.
	 *
	 * @param element the element
	 */
	private static void visitElement_IRCUsers(org.w3c.dom.Element element)
	{
		org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++)
		{
			org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
			if (attr.getName().equals("lastId"))
			{
				lastId = Integer.parseInt(attr.getValue());
			}
		}
		
		org.w3c.dom.NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++)
		{
			org.w3c.dom.Node node = nodes.item(i);
			switch (node.getNodeType())
			{
				case org.w3c.dom.Node.CDATA_SECTION_NODE:
					break;
				case org.w3c.dom.Node.ELEMENT_NODE:
					org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
					if (nodeElement.getTagName().equals("IRCUserInfo"))
					{
						listUsers.add(visitElement_IRCUserInfo(nodeElement));
					}
					break;
				case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
					break;
			}
		}
	}
	
	/**
	 * Visit element_ irc user info.
	 *
	 * @param element the element
	 * @return the iRC user informations
	 */
	public static IRCUserInformations visitElement_IRCUserInfo(org.w3c.dom.Element element)
	{
		int id = -1;
		String nickname = "";
		String login = "";
		String password = "";
		boolean hasPicture = false;

		org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++)
		{
			org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
			if (attr.getName().equals("id"))
			{
				id = Integer.parseInt(attr.getValue());
			}
			if (attr.getName().equals("nickname"))
			{
				nickname = attr.getValue();
			}
			if (attr.getName().equals("login"))
			{
				login = attr.getValue();
			}
			if (attr.getName().equals("password"))
			{
				password = attr.getValue();
			}
			if (attr.getName().equals("hasPicture"))
			{
				hasPicture = Boolean.parseBoolean(attr.getValue());
			}
		}
		org.w3c.dom.NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++)
		{
			org.w3c.dom.Node node = nodes.item(i);
			switch (node.getNodeType())
			{
				case org.w3c.dom.Node.CDATA_SECTION_NODE:
					break;
				case org.w3c.dom.Node.ELEMENT_NODE:
//					org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
//					if (nodeElement.getTagName().equals(""))
//					{
//					}
					break;
				case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
					break;
			}
		}
		
		return new IRCUserInformations(id, nickname, login, password, hasPicture);
	}
	
	
	/**
	 * Gets the list user infomation.
	 *
	 * @return the list user infomation
	 */
	public static List<IRCUserInformations> getListUserInfomation() {
		return listUsers;
	}
	
	/**
	 * Gets the last id.
	 *
	 * @return the last id
	 */
	public static int getLastId() {
		return lastId;
	}
}
