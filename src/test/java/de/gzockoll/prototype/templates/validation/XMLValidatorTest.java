package de.gzockoll.prototype.templates.validation;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class XMLValidatorTest {

    @Test
    public void testValidation() throws IOException, SAXException, ParserConfigurationException {
        assertThat(true).isTrue();
        XMLValidator validator=new XMLValidator("http://www.w3.org/2007/schema-for-xslt20.xsd","http://svn.apache.org/repos/asf/xmlgraphics/fop/trunk/src/foschema/fop.xsd");
        String xml="" +
                "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\" version=\"1.0\">\n" +
                "    <xsl:output encoding=\"UTF-8\" indent=\"yes\" method=\"xml\" standalone=\"no\" omit-xml-declaration=\"no\"/></xsl:stylesheet>";
        validator.validate(xml);
    }
}