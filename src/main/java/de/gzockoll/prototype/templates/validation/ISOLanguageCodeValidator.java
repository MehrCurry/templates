package de.gzockoll.prototype.templates.validation;

import com.google.common.collect.Sets;
import com.vaadin.data.Validator;
import de.gzockoll.prototype.templates.entity.LanguageCode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Locale;

public class ISOLanguageCodeValidator implements ConstraintValidator<ValidISOLanguageCode,String>, Validator {
    public static final HashSet<String> VALID_LANGUAGES = Sets.newHashSet(Locale.getISOLanguages());

    @Override
    public void initialize(ValidISOLanguageCode validISOLanguageCode) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return VALID_LANGUAGES.contains(s);
    }

    boolean isValid(String s) {
        return isValid(s,null);
    }

    @Override
    public void validate(Object o) throws InvalidValueException {
        LanguageCode code= (LanguageCode) o;
        code.validate();
    }
}
