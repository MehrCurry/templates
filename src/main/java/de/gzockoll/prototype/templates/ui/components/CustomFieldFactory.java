package de.gzockoll.prototype.templates.ui.components;

import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import de.gzockoll.prototype.templates.entity.AbstractEntity;
import de.gzockoll.prototype.templates.entity.LanguageCode;

import java.util.Date;

public class CustomFieldFactory extends DefaultFieldGroupFieldFactory {
    @Override
    public <T extends Field> T createField(Class<?> type, Class<T> fieldType) {
        if (Date.class.isAssignableFrom(type)) {
            return (T) new DateField();
        }
        if (AbstractEntity.class.isAssignableFrom(type)) {
            return (T) new ComboBox();
        }
        if (LanguageCode.class.isAssignableFrom(type)) {
            return (T) new LanguageCodeField();
        }
        return super.createField(type, fieldType);
    }
}
