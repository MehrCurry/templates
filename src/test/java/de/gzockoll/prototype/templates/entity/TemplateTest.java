package de.gzockoll.prototype.templates.entity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TemplateTest {

    @Test
    public void testStates() {
        Template t=new Template("d").requestApproval().approve();
        assertThat(t).isNotNull();
        assertThat(t.isApproved()).isTrue();
    }
}