package com.gc.irc.server.service.file.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.server.model.UserInformations;

/**
 * A scanner to parse an identification xml file.
 *
 * @version 0.0.4
 * @author x472511
 */
public final class UserInformationScanner extends AbstractLoggable {

    /** The last id. */
    private int lastId = 0;

    /** The m document. */
    private org.w3c.dom.Document mDocument;

    /** The list users. */
    private Map<Long, UserInformations> users = new ConcurrentHashMap<Long, UserInformations>();

    /**
     * Instantiates a new user imformation scanner.
     *
     * @param document
     *            the document
     */
    public UserInformationScanner(final org.w3c.dom.Document document) {
        mDocument = document;
        visitDocument();
    }

    /**
     * Instantiates a new user imformation scanner.
     *
     * @param file
     *            the file
     * @throws javax.xml.parsers.ParserConfigurationException
     *             the parser configuration exception
     * @throws org.xml.sax.SAXException
     *             the sAX exception
     * @throws java.io.IOException
     *             Signals that an I/O exception has occurred.
     */
    public UserInformationScanner(final String file) throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = builderFactory.newDocumentBuilder();
        mDocument = builder.parse(new InputSource(new File(file).toURI().toString()));
        visitDocument();
    }

    /**
     * Gets the last id.
     *
     * @return the last id
     */
    public int getLastId() {
        return lastId;
    }

    /**
     * Gets the list user infomation.
     *
     * @return the list user infomation
     */
    public Map<Long, UserInformations> getListUserInfomation() {
        return users;
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

    /**
     * Visit element_ irc user info.
     *
     * @param element
     *            the element
     * @return the iRC user informations
     */
    private UserInformations visitElementIRCUserInfo(final org.w3c.dom.Element element) {
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
    private void visitElementIRCUsers(final org.w3c.dom.Element element) {
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
                    final UserInformations userInfo = visitElementIRCUserInfo(nodeElement);
                    users.put(userInfo.getId(), userInfo);
                }
                break;
            case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                break;
            }
        }
    }
}
