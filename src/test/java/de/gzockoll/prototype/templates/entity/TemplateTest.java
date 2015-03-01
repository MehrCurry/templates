package de.gzockoll.prototype.templates.entity;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class TemplateTest {

    @Test
    public void testStates() {
        Template t=new Template("d").saveContent(new ByteArrayInputStream("JUnit".getBytes())).requestApproval().approve();
        assertThat(t).isNotNull();
        assertThat(t.isApproved()).isTrue();
    }

    @Test
    public void testContent() {
        Template t=new Template("de");
        t.saveContent(ClassLoader.getSystemResourceAsStream("produkte.pdf"));
        assertThat(t).isNotNull();
    }
}