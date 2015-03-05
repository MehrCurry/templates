package de.gzockoll.prototype.templates.ui.view;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
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
    private TextField language=new TextField("Language");
    private ComboBox templates=new ComboBox("XSL Transformer");
    private ComboBox stationery = new ComboBox("Stationery");
    private View viewChangeListener;

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
        layout.addComponent(saveButton);

        binder.setBuffered(true);

        layout.addComponent(table);

        layout.addComponent(language);
        layout.addComponent(templates);
        layout.addComponent(stationery);
        group.bindMemberFields(this);

        setCompositionRoot(layout);
    }

    public void setTemplateDataSource(Item item) {
        binder.setItemDataSource(item);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if (viewChangeListener!=null) {
            viewChangeListener.enter(viewChangeEvent);
        }
    }

    public void setViewChangeListener(View viewChangeListener) {
        this.viewChangeListener = viewChangeListener;
    }
}
