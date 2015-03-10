package de.gzockoll.prototype.templates.validation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class XMLValidatorTest {
    @Rule
    public ExpectedException thrown=ExpectedException.none();

    private final XMLValidator validator = new XMLValidator("http://www.w3.org/2007/schema-for-xslt20.xsd","http://svn.apache.org/repos/asf/xmlgraphics/fop/trunk/src/foschema/fop.xsd");

    @Test
    public void testValidation() throws IOException, SAXException, ParserConfigurationException {
        String xml="" +
                "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\" version=\"1.0\">\n" +
                "    <xsl:output encoding=\"UTF-8\" indent=\"yes\" method=\"xml\" standalone=\"no\" omit-xml-declaration=\"no\"/></xsl:stylesheet>";
        validator.validate(xml);
    }

    @Test
    public void testValidationWithInvalidContent() throws IOException, SAXException, ParserConfigurationException {
        thrown.expect(SAXParseException.class);
        String xml="" +
                "Invalid! <xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\" version=\"1.0\">\n" +
                "    <xsl:output encoding=\"UTF-8\" indent=\"yes\" method=\"xml\" standalone=\"no\" omit-xml-declaration=\"no\"/></xsl:stylesheet>";
        validator.validate(xml);
    }

}