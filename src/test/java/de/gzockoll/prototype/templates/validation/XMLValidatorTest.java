package de.gzockoll.prototype.templates.validation;

import de.gzockoll.prototype.templates.entity.AbstractEntity;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class XMLValidatorTest {

    @Test
    public void testValidation() {
        assertThat(true).isTrue();

        TestClass obj=new TestClass("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertThat(obj.isValid()).isTrue();
    }

    public static final class TestClass extends AbstractEntity {

        @ValidIXMLData(schemata = "http://www.w3.org/1999/XSL/Transform,http://www.w3.org/1999/XSL/Format")
        private String xmlData;

        public TestClass(String xmlData) {
            this.xmlData = xmlData;
        }
    }

}