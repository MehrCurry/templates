package de.gzockoll.prototype.templates.ui.view;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import de.gzockoll.prototype.templates.entity.Template;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;

import javax.annotation.PostConstruct;

@VaadinUIScope
@Getter
@Slf4j
@VaadinView(name = "templateView")
public class TemplateView extends CustomComponent implements View {
    private Table table=new Table();
    private Button preview=new Button("Preview");
    private BeanFieldGroup<Template> binder=new BeanFieldGroup<>(Template.class);
    private Button saveButton=new Button("Save");

    @PostConstruct
    public void init() {
        VerticalLayout layout=new VerticalLayout();
        layout.setSizeFull();
        layout.addComponent(preview);
        layout.addComponent(binder.buildAndBind("Language", "language"));

        binder.setBuffered(true);

        layout.addComponent(table);
        setCompositionRoot(layout);
    }

    public void setTemplateDataSource(Item item) {
        binder.setItemDataSource(item);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
