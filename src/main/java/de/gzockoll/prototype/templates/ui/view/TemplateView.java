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
import org.tylproject.vaadin.addon.datanav.ButtonBar;
import org.tylproject.vaadin.addon.datanav.NavigationLabel;
import org.tylproject.vaadin.addon.fieldbinder.FieldBinder;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;
import org.vaadin.viritin.FilterableListContainer;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;

@VaadinUIScope
@Getter
@Slf4j
@VaadinView(name = "templateView")
public class TemplateView extends CustomComponent implements View {
    private Table table=new Table();
    private Button preview=new Button("Preview");
    private BeanFieldGroup<Template> group=new BeanFieldGroup<>(Template.class);
    private Button saveButton=new Button("Save");

    // initialize an empty container
    final FilterableListContainer<Template> container =
            new FilterableListContainer<Template>(Template.class);

    // FIELD BINDER
    // initialize the FieldBinder for the given container
    final FieldBinder<Template> binder = new FieldBinder<Template>(Template.class, container);

    @PostConstruct
    public void init() {
        VerticalLayout layout=new VerticalLayout();
        layout.setSizeFull();
        layout.addComponent(preview);
        layout.addComponent(group.buildAndBind("Language", "language"));
        layout.addComponent(group.buildAndBind("XSL Template", "transform"));
        layout.addComponent(group.buildAndBind("Stationery", "stationery"));
        layout.addComponent(saveButton);

        binder.setBuffered(true);

        layout.addComponent(table);

        VerticalLayout formLayout = new MVerticalLayout(
            new ButtonBar(binder.getNavigation().withDefaultBehavior()),
            new MFormLayout(
                    binder.build("language"),
                    binder.build("transform"),
                    binder.build("stationery"),

                    new NavigationLabel(binder.getNavigation())
            ).withFullWidth().withMargin(true)
        ).withFullWidth().withMargin(true);

        layout.addComponent(formLayout);
        setCompositionRoot(layout);
    }

    public void setTemplateDataSource(Item item) {
        binder.setItemDataSource(item);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
