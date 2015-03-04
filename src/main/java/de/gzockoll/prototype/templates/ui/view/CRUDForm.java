package de.gzockoll.prototype.templates.ui.view;

import com.google.common.collect.Sets;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.ui.*;
import de.gzockoll.prototype.templates.entity.AbstractEntity;

import java.util.Collection;
import java.util.HashSet;

import static com.vaadin.event.Action.Handler;

public class CRUDForm<T extends AbstractEntity> extends CustomComponent {
    private static final int MAX_PAGE_LENGTH = 20;
    private final Class<T> clazz;
    private final Collection<String> hiddenProperties = new HashSet<>();
    private VerticalLayout layout = new VerticalLayout();
    private Table table = new Table();
    private BeanItemContainer<T> tableContainer;
    private FieldGroup fieldGroup;
    private Action actionDelete = new Action("Delete");

    public CRUDForm(Class<T> clazz, String... hiddenProperties) {
        this.clazz = clazz;
        this.hiddenProperties.addAll(Sets.newHashSet(hiddenProperties));
        initTable();
        layout.addComponent(createAddButton());
        layout.addComponent(table);
        table.setPageLength(20);
        setCompositionRoot(layout);
    }

    private Component createAddButton() {
        Button button = new Button("Add");
        button.addClickListener(event -> {
            try {
                openDetailWindow(new BeanItem<T>(clazz.newInstance()), "Add " + clazz.getSimpleName());
            } catch (InstantiationException | IllegalArgumentException | IllegalAccessException e) {
                handleError(e);
            }
        });
        return button;
    }

    private void initTable() {
        tableContainer = new BeanItemContainer<T>(clazz);
        table.setContainerDataSource(tableContainer);
        table.setSelectable(true);
        table.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.isDoubleClick()) {
                openDetailWindow((BeanItem) itemClickEvent.getItem(), "Edit " + clazz.getSimpleName());
            }
        });
        table.addActionHandler(new Handler() {

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                if (actionDelete == action) {
                    tableContainer.removeItem(target);
                    updateTable();
                }

            }

            @Override
            public Action[] getActions(Object o, Object o1) {
                return new Action[]{actionDelete};
            }
        });
    }

    private void updateTable() {
        if (table.size() > MAX_PAGE_LENGTH) {
            table.setPageLength(MAX_PAGE_LENGTH);
        } else {
            table.setPageLength(table.size());
        }
        table.markAsDirtyRecursive();
    }

    private void openDetailWindow(BeanItem item, String s) {
        Window window = new Window();
        window.setModal(true);

        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        window.setContent(layout);

        fieldGroup = new BeanFieldGroup<T>(clazz);
        fieldGroup.setItemDataSource(item);

        fieldGroup.getUnboundPropertyIds().stream().forEach(e -> {
            T bean = (T) item.getBean();
            if (!hiddenProperties.contains(e)) {
                Field<?> field = fieldGroup.buildAndBind(e);
                field.setReadOnly(false);
                layout.addComponent(field);
            }
        });
        layout.addComponent(createOkButton(window));
        layout.addComponent(createCancelButton(window));
        getUI().addWindow(window);
    }

    private Component createCancelButton(Window window) {
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> window.close());
        return cancelButton;
    }

    private Component createOkButton(Window window) {
        Button okButton = new Button("OK");
        okButton.addClickListener(e -> {
                    try {
                        fieldGroup.commit();
                        BeanItem<T> beanItem = (BeanItem<T>) fieldGroup.getItemDataSource();
                        tableContainer.addItem(beanItem);
                        updateTable();
                        window.close();
                    } catch (FieldGroup.CommitException ex) {
                        handleError(ex);
                    }
                }
        );
        return okButton;
    }

    private void handleError(Exception e) {
        Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
    }

    public void setBeanItemContainerDataSource(BeanItemContainer<T> container) {
        this.tableContainer = container;
        updateTable();
    }

}
