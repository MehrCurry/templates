package de.gzockoll.prototype.templates.validation;

import de.gzockoll.prototype.templates.entity.AbstractEntity;
import lombok.Data;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class ISOLanguageCodeValidatorTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testIsValid() throws Exception {
        final Customer customer = new Customer();
        assertThat(customer.isValid()).isFalse();

        customer.setName("JUnit");
        assertThat(customer.isValid()).isFalse();

        customer.setLastname("Test");
        assertThat(customer.isValid()).isFalse();

        customer.setLanguage("de");
        assertThat(customer.isValid()).isTrue();

        customer.setLanguage("--");
        assertThat(customer.isValid()).isFalse();
    }

    @Test
    public void testGetErrors() {
        final Customer customer = new Customer();

        customer.setName("JUnit");
        customer.setLastname("");
        customer.setLanguage("--");

        assertThat(customer.validationErrors()).hasSize(2);

    }

    @Test
    public void testValidator() {
        ISOLanguageCodeValidator validator=new ISOLanguageCodeValidator();

        Arrays.asList("de,en,uk,fr".split(",")).stream().forEach(s ->
                assertThat(validator.isValid(s)).isTrue()
        );
        Arrays.asList("DE,,XXXXX,-.".split(",")).stream().forEach(s ->
                        assertThat(validator.isValid(s)).isFalse()
        );
    }

    @Test
    public void testValidate() {
        final Customer customer = new Customer();

        customer.setName("JUnit");
        customer.setLastname("");
        customer.setLanguage("--");

        thrown.expect(ConstraintViolationException.class);
        customer.validate();
    }

    @Test
    public void voidTestNullValue() {
        NullIsValid entity = new NullIsValid();
        assertThat(entity.isValid()).isTrue();

        NullIsInValid another = new NullIsInValid();
        assertThat(another.isValid()).isFalse();
        another.setLanguage(Locale.getDefault().getLanguage());
        assertThat(another.isValid()).isTrue();
    }

    @Data
    private static class Customer extends AbstractEntity {
        @NotNull
        private String name;

        @Size(min=1)
        private String lastname;

        @ValidISOLanguageCode
        private String language;
    }

    @Data
    private static class NullIsValid extends AbstractEntity {
        @ValidISOLanguageCode(allowNull = true)
        private String language;
    }

    @Data
    private static class NullIsInValid extends AbstractEntity {
        @ValidISOLanguageCode(allowNull = false)
        private String language;
    }
}