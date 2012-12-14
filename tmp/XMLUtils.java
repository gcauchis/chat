package com.acp.acs.common.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.acp.acs.common.exception.XMLException;

/**
 * The Class XMLUtils.
 */
public final class XMLUtils {

    /**
     * Instantiates a new xML utils.
     */
    private XMLUtils() {
        super();
    }

    /**
     * Extract element.
     * 
     * @param xml
     *            the xml
     * @param name
     *            the name
     * @return the string
     */
    public static String extractElement(final String xml, final String name) {
        if (xml.indexOf("<" + name) > 0) {
            return xml.substring(xml.indexOf("<" + name), xml.indexOf("</" + name + ">") + name.length() + 3);
        }
        return "";
    }

    /**
     * Checks for element.
     * 
     * @param xml
     *            the xml
     * @param name
     *            the name
     * @return true, if successful
     */
    public static boolean hasElement(final String xml, final String name) {
        return StringUtils.isNotEmpty(extractElement(xml, name));
    }

    /**
     * Checks if is root element.
     * 
     * @param xml
     *            the xml
     * @param name
     *            the name
     * @return true, if is root element
     */
    public static boolean isRootElement(final String xml, final String name) {
        if (StringUtils.isNotEmpty(xml) && StringUtils.isNotEmpty(name)) {
            return removeHeader(xml).trim().indexOf("<" + name) == 0;
        }
        return false;
    }

    /**
     * Removes the element.
     * 
     * @param xml
     *            the xml
     * @param name
     *            the name
     * @return the string
     */
    public static String removeElement(final String xml, final String name) {
        final String xmlElement = extractElement(xml, name);
        if (StringUtils.isNotEmpty(xmlElement)) {
            return xml.replace(xmlElement, "");
        }
        return xml;
    }

    /**
     * Removes the header.
     * 
     * @param xml
     *            the xml
     * @return the string
     */
    public static String removeHeader(final String xml) {
        if (StringUtils.isEmpty(xml) || xml.indexOf("<?") < 0) {
            return xml;
        }
        return xml.substring(xml.indexOf("?>") + 2);
    }

    /**
     * Format.
     * 
     * @param unformattedXml
     *            the unformatted xml
     * @return the string
     * @throws XSDException
     */
    public static String format(final String unformattedXml) throws XMLException {
        Transformer transformer;
        try {
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
            throw new XMLException(e);
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        // initialize StreamResult with File object to save to file
        final StreamResult result = new StreamResult(new StringWriter());
        final DOMSource source = new DOMSource(parseXmlFile(unformattedXml));
        try {
            transformer.transform(source, result);
        } catch (final TransformerException e) {
            throw new XMLException(e);
        }
        return result.getWriter().toString();
    }

    /**
     * Parses the xml file.
     * 
     * @param xml
     *            the in
     * @return the document
     * @throws XSDException
     */
    public static Document parseXmlFile(final String xml) throws XMLException {
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();
            final InputSource is = new InputSource(new StringReader(xml));
            return db.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XMLException("Fail to Parse xml", e);
        }
    }

}
