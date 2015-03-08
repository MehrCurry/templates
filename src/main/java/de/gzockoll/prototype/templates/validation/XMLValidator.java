package de.gzockoll.prototype.templates.validation;

import com.google.common.collect.ImmutableSet;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * Created by Guido on 07.03.2015.
 */
public class XMLValidator implements ConstraintValidator<ValidIXMLData, String>, com.vaadin.data.Validator {

    private Set<String> schemata = Collections.emptySet();

    @Override
    public void initialize(ValidIXMLData validIXMLData) {
        this.schemata = ImmutableSet.copyOf(validIXMLData.schemata().split(","));
    }

    public boolean isValid(String s) {
        try {
            validate(s);
            return true;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return false;
        }
    }

    public void validate(String s) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parser.parse(new ByteArrayInputStream(s.getBytes()));

        // create a SchemaFactory capable of understanding WXS schemas
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Object[] sources = schemata.stream().map(e -> {
            try {
                return new StreamSource(new URL(e).openStream());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }).toArray();
        Schema schema=factory.newSchema();
        // create a Validator instance, which can be used to validate an instance document
        Validator validator = schema.newValidator();

        // validate the DOM tree
        validator.validate(new DOMSource(document));
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            validate(s);
            return true;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("could not parse xml: "+ e.getMessage());
            return false;
        }
    }

    @Override
    public void validate(Object o) throws InvalidValueException {
        try {
            validate(o.toString());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new InvalidValueException(e.getMessage());
        }
    }
}
