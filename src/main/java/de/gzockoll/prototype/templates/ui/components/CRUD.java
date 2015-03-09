package de.gzockoll.prototype.templates.ui.components;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.*;
import de.gzockoll.prototype.templates.entity.AbstractEntity;


/**
 * Created by Guido on 09.03.2015.
 */
public class CRUD<T extends AbstractEntity> extends HorizontalSplitPanel {
    private static final Action ACTION_ADD = new Action("Add");
    private static final Action ACTION_DELETE = new Action("Delete");
    private final Class clazz;
    private int id = 0;
    private BeanItemContainer<T> container;

    public CRUD(Class clazz) {
        this.clazz=clazz;
        container=new BeanItemContainer<T>(clazz);
    }

    private Component createTable(BeanItemContainer<T> container) {
        Table table=new Table(null,container);
        table.setSelectable(true);
        table.setSizeFull();
        table.addItemClickListener(e-> {
            if (MouseEventDetails.MouseButton.LEFT.getName().equals(e.getButtonName())) {
                setSecondComponent(createForm(e.getItem()));
            }
        });
        table.addActionHandler(new Action.Handler() {
            @Override
            public Action[] getActions(Object o, Object o1) {
                return new Action[0];
            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                if (ACTION_DELETE == action) {
                    container.removeItem(target);
                }
                if (ACTION_ADD == action) {
                    try {
                        container.addBean((T) clazz.newInstance());
                    } catch (IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return table;
    }

    private Component createForm(Item item) {
        FormLayout layout = new FormLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        FieldGroup group = new FieldGroup(item);
        group.setFieldFactory(new CustomFieldFactory());
        group.getUnboundPropertyIds().forEach(p ->
                layout.addComponent(group.buildAndBind(p)));
        Button button = new Button(("Commit"));
        button.addClickListener(e -> {
            try {
                group.commit();
            } catch (FieldGroup.CommitException ex) {
                Notification.show(ex.getCause().getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });
        layout.addComponent(button);
        return layout;
    }

    public void setContainer(BeanItemContainer<T> container) {
        this.container=container;
        setFirstComponent(createTable(container));
    }
}
