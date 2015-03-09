package de.gzockoll.prototype.templates.ui.converter;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;
import de.gzockoll.prototype.templates.entity.LanguageCode;

public class ConverterFactory extends DefaultConverterFactory {
    @Override
    public <PRESENTAION, MODEL> Converter<PRESENTAION, MODEL> createConverter(Class<PRESENTAION> sourceType,Class<MODEL> modelType) {
        if (String.class == sourceType && LanguageCode.class == modelType) {
            return (Converter<PRESENTAION, MODEL>) new LanguageCodeToStringConverter();
        }

        return super.createConverter(sourceType, modelType);
    }
}
