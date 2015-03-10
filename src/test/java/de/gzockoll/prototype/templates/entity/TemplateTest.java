package de.gzockoll.prototype.templates.entity;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateTest {

    @Test
    public void testEquals() {
        Template t1=new Template("de");
        Template t2=new Template("fr");
        Template t3=new Template("de");

        assertThat(t1).isEqualTo(t1);
        assertThat(t1.hashCode()).isEqualTo(t1.hashCode());

        assertThat(t1).isNotEqualTo(t2);
        assertThat(t1.hashCode()).isNotEqualTo(t2.hashCode());

        assertThat(t2).isNotEqualTo(t3);
        assertThat(t2.hashCode()).isNotEqualTo(t3.hashCode());

        assertThat(t1).isEqualTo(t3);
        assertThat(t1.hashCode()).isEqualTo(t3.hashCode());
    }

    @Test
    public void testContains() {
        Template t1=new Template("de");
        Template t2=new Template("fr");
        Template t3=new Template("de");

        Set<Template> set=new HashSet<>();
        set.add(t1);
        assertThat(set.contains(t1)).isTrue();
        assertThat(set.contains(t2)).isFalse();
        assertThat(set.contains(t3)).isTrue();

    }
}