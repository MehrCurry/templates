package de.gzockoll.prototype.templates.ui.components;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import de.gzockoll.prototype.templates.entity.AbstractEntity;
import de.gzockoll.prototype.templates.entity.Template;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;


@Getter
public class CRUD<T extends AbstractEntity> extends HorizontalSplitPanel {
    private final Class clazz;
    private  Grid grid;
    private Map<String, Field> fields=new HashMap<>();
    private BeanItemContainer<T> container;
    private Button commitButton=new Button("Speichern");
    private FieldGroup fieldGroup;
    private FormLayout detailForm;
    private Object item;
    private Map<String,Container> containerMap = new HashMap<>();
    private ButtonBar buttonBar=new ButtonBar(Collections.EMPTY_LIST);
    private Button addButton=new Button("Add");
    private Button deleteButton = new Button("Delete");

    public CRUD(Class clazz) {
        this.clazz=clazz;

        container=new BeanItemContainer<T>(clazz);
        setFirstComponent(createTable(container));
    }

    private Component createTable(BeanItemContainer<T> container) {
        VerticalLayout main=new VerticalLayout();
        grid=new Grid(null,container);
        grid.setSizeFull();
        main.addComponent(grid);
        HorizontalLayout buttons=new HorizontalLayout();
        buttons.addComponent(addButton);
        buttons.addComponent(deleteButton);
        deleteButton.setEnabled(false);
        main.addComponent(buttons);
        return main;
    }

    private FormLayout createForm(Item item) {
        checkArgument(item!=null);
        FormLayout layout = new FormLayout();
        layout.setSpacing(false);
        layout.setMargin(true);
        fieldGroup = new FieldGroup(item);
        fieldGroup.setFieldFactory(new CustomFieldFactory());
        fieldGroup.getUnboundPropertyIds().forEach(p -> {
            final Field field = fieldGroup.buildAndBind(p);
            fields.put((String) p,field);
            if (containerMap.containsKey(p)) {
                ((AbstractSelect)field).setContainerDataSource(containerMap.get(p));
            }
            layout.addComponent(field);
        });
        layout.addComponent(commitButton);
        layout.addComponent(buttonBar);
        return layout;
    }

    public void commit() {
        try {
            fieldGroup.commit();
        } catch (FieldGroup.CommitException e) {
            Notification.show(e.getCause().getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }
    public void setContainer(BeanItemContainer<T> container) {
        grid.setContainerDataSource(container);
    }

    public void setItem(BeanItem<Template> item) {
        if (item!=null) {
            if (fieldGroup == null) {
                detailForm = createForm(item);
                setSecondComponent(detailForm);
            }
            fieldGroup.setItemDataSource(item);

        }
    }

    public Optional<? extends Field> getField(String property) {
        return Optional.ofNullable(fields.get(property));
    }

    public void addDatasourceForProperty(String key,Container container) {
        containerMap.put(key, container);
    }
}
