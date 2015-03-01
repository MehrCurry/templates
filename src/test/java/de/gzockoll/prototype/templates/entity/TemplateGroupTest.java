package de.gzockoll.prototype.templates.entity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateGroupTest {
    @Rule
    public ExpectedException thrown=ExpectedException.none();

    @Test
    public void testActivate() throws Exception {
        Template de=new Template(Locale.GERMAN.getLanguage());

        TemplateGroup group=new TemplateGroup(1,"de","junit");
        group.addTemplate(de);

        assertThat(group.getActiveTemplate().isPresent()).isFalse();
        de.requestApproval().approve();
        group.activate(de);
        Optional<Template> option = group.getActiveTemplate();
        assertThat(option.isPresent()).isTrue();
        assertThat(option.get()).isEqualTo(de);
    }

    @Test
    public void testHasSameLanguageAs() throws Exception {
        Template de=new Template(Locale.GERMAN.getLanguage());
        Template uk=new Template(Locale.UK.getLanguage());

        TemplateGroup group=new TemplateGroup(1,"de","junit");

        assertThat(group.hasSameLanguageAs(de)).isTrue();
        assertThat(group.hasSameLanguageAs(uk)).isFalse();
    }

    @Test
    public void testAddWithWrongLanguage() throws Exception {
        thrown.expect(IllegalArgumentException.class);

        Template uk=new Template(Locale.UK.getLanguage());

        TemplateGroup group=new TemplateGroup(1,"de","junit");
        group.addTemplate(uk);
    }
}