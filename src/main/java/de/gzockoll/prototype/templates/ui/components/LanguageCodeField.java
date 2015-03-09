package de.gzockoll.prototype.templates.ui.components;

import com.vaadin.ui.TextField;
import de.gzockoll.prototype.templates.ui.converter.LanguageCodeToStringConverter;
import de.gzockoll.prototype.templates.validation.ISOLanguageCodeValidator;

/**
 * Created by Guido on 09.03.2015.
 */
public class LanguageCodeField extends TextField {
    public LanguageCodeField() {
        super();
        setConverter(new LanguageCodeToStringConverter());
        addValidator(new ISOLanguageCodeValidator());
    }

    public LanguageCodeField(String caption) {
        super(caption);
        setConverter(new LanguageCodeToStringConverter());
        addValidator(new ISOLanguageCodeValidator());
    }
}
