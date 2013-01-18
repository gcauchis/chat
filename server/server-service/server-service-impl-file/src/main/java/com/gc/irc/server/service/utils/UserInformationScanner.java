package com.gc.irc.server.service.utils;

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

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.server.model.UserInformations;

/**
 * A scanner to parse an identification xml file.
 * 
 * @author gcauchis
 * 
 */
public final class UserInformationScanner extends AbstractLoggable {

    /** The last id. */
    private static int lastId = 0;

    /** The list users. */
    private static List<UserInformations> listUsers = Collections.synchronizedList(new ArrayList<UserInformations>());

    /**
     * Gets the last id.
     * 
     * @return the last id
     */
    public static int getLastId() {
        return lastId;
    }

    /**
     * Gets the list user infomation.
     * 
     * @return the list user infomation
     */
    public static List<UserInformations> getListUserInfomation() {
        return listUsers;
    }

    /**
     * Visit element_ irc user info.
     * 
     * @param element
     *            the element
     * @return the iRC user informations
     */
    private static UserInformations visitElementIRCUserInfo(final org.w3c.dom.Element element) {
        int id = -1;
        String nickname = "";
        String login = "";
        String password = "";
        boolean hasPicture = false;

        final org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            final org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals("id")) {
                id = Integer.parseInt(attr.getValue());
            }
            if (attr.getName().equals("nickname")) {
                nickname = attr.getValue();
            }
            if (attr.getName().equals("login")) {
                login = attr.getValue();
            }
            if (attr.getName().equals("password")) {
                password = attr.getValue();
            }
            if (attr.getName().equals("hasPicture")) {
                hasPicture = Boolean.parseBoolean(attr.getValue());
            }
        }
        final org.w3c.dom.NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            final org.w3c.dom.Node node = nodes.item(i);
            switch (node.getNodeType()) {
            case org.w3c.dom.Node.CDATA_SECTION_NODE:
                break;
            case org.w3c.dom.Node.ELEMENT_NODE:
                // org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
                // if (nodeElement.getTagName().equals(""))
                // {
                // }
                break;
            case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                break;
            }
        }

        return new UserInformations(id, nickname, login, password, hasPicture);
    }

    /**
     * Visit element_ irc users.
     * 
     * @param element
     *            the element
     */
    private static void visitElementIRCUsers(final org.w3c.dom.Element element) {
        final org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            final org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals("lastId")) {
                lastId = Integer.parseInt(attr.getValue());
            }
        }

        final org.w3c.dom.NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            final org.w3c.dom.Node node = nodes.item(i);
            switch (node.getNodeType()) {
            case org.w3c.dom.Node.CDATA_SECTION_NODE:
                break;
            case org.w3c.dom.Node.ELEMENT_NODE:
                final org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
                if (nodeElement.getTagName().equals("IRCUserInfo")) {
                    listUsers.add(visitElementIRCUserInfo(nodeElement));
                }
                break;
            case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                break;
            }
        }
    }

    /** The m document. */
    private org.w3c.dom.Document mDocument;

    /**
     * Instantiates a new user imformation scanner.
     * 
     * @param document
     *            the document
     */
    public UserInformationScanner(final org.w3c.dom.Document document) {
        mDocument = document;
        listUsers = Collections.synchronizedList(new ArrayList<UserInformations>());
        visitDocument();
    }

    /**
     * Instantiates a new user imformation scanner.
     * 
     * @param file
     *            the file
     * @throws ParserConfigurationException
     *             the parser configuration exception
     * @throws SAXException
     *             the sAX exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public UserInformationScanner(final String file) throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = builderFactory.newDocumentBuilder();
        mDocument = builder.parse(new InputSource(new File(file).toURI().toString()));
        listUsers = Collections.synchronizedList(new ArrayList<UserInformations>());
        visitDocument();
    }

    /**
     * Scan through org.w3c.dom.Document mDocument.
     */
    public void visitDocument() {
        final org.w3c.dom.Element element = mDocument.getDocumentElement();
        if ((element != null) && element.getTagName().equals("IRCUsers")) {
            visitElementIRCUsers(element);
        }
    }
}
