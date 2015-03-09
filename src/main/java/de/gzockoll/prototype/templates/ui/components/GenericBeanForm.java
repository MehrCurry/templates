package de.gzockoll.prototype.templates.ui.components;

import com.vaadin.client.metadata.Property;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import de.gzockoll.prototype.templates.entity.Template;
import lombok.extern.slf4j.Slf4j;

import static com.google.common.base.Preconditions.checkState;

@Slf4j
public class GenericBeanForm<T> extends FormLayout {

    private final FieldGroup fieldGroup;

    public GenericBeanForm(Class clazz) {
        setSizeUndefined();
        setMargin(true);

        fieldGroup = new BeanFieldGroup<T>(clazz);
        fieldGroup.setFieldFactory(new CustomFieldFactory());
        setItemDataSource(new BeanItem<Template>(new Template()));

        for (Object prop:fieldGroup.getUnboundPropertyIds()) {
            log.debug("Prop: " + prop.toString());
            // Field<?> field = fieldGroup.buildAndBind(prop);
            // addComponent(field);
        }
    }

    public void setItemDataSource(Item dataSource) {
        checkState(fieldGroup!=null);
        fieldGroup.setItemDataSource(dataSource);
    }
}
