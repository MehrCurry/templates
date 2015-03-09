package de.gzockoll.prototype.templates.ui.converter;

import com.vaadin.data.util.converter.Converter;
import de.gzockoll.prototype.templates.entity.LanguageCode;

import java.util.Locale;

/**
 * Created by Guido on 09.03.2015.
 */
public class LanguageCodeToStringConverter implements Converter<String,LanguageCode> {
    @Override
    public LanguageCode convertToModel(String s, Class<? extends LanguageCode> aClass, Locale locale) throws ConversionException {
        return new LanguageCode(s);
    }

    @Override
    public String convertToPresentation(LanguageCode languageCode, Class<? extends String> aClass, Locale locale) throws ConversionException {
        return languageCode.getCode();
    }

    @Override
    public Class<LanguageCode> getModelType() {
        return LanguageCode.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
