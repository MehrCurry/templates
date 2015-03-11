package de.gzockoll.prototype.templates.ui.view;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import de.gzockoll.prototype.templates.entity.Template;
import de.gzockoll.prototype.templates.ui.components.ButtonBar;
import de.gzockoll.prototype.templates.ui.components.CRUD;
import de.gzockoll.prototype.templates.ui.components.GenericBeanForm;
import de.gzockoll.prototype.templates.ui.components.OnDemandStreamSourceProxy;
import de.gzockoll.prototype.templates.util.Command;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;

import javax.annotation.PostConstruct;
import java.util.Collection;

@VaadinUIScope
@Getter
@Slf4j
@VaadinView(name = "templateView")
public class TemplateView extends CustomComponent implements View {
    private View viewChangeListener;
    private TextArea editor=new TextArea();
    private OnDemandStreamSourceProxy streamSource=new OnDemandStreamSourceProxy();
    private BrowserWindowOpener browserWindowOpener=new BrowserWindowOpener(new StreamResource(streamSource,"preview.pdf"));
    private Button newButton=new Button("Neu");
    private CRUD<Template> crud=new CRUD<Template>(Template.class);
    private ButtonBar buttonBar;
    private final VerticalLayout left = new VerticalLayout();

    @PostConstruct
    public void init() {
        HorizontalLayout mainLayout=new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        left.setSizeFull();

        left.addComponent(editor);
        editor.setImmediate(true);
        editor.setWidth("100%");
        editor.setHeight("200px");

        mainLayout.addComponent(left);
        left.addComponent(new GenericBeanForm<Template>(Template.class));
        left.addComponent(crud);
        setCompositionRoot(mainLayout);
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

    public void setItem(BeanItem<Template> item) {
        crud.setItem(item);
    }

    public void select(Template aTemplate) {
        crud.getTable().select(aTemplate);
    }

    public void commit() {
        crud.commit();
    }

    public void setContainer(BeanItemContainer<Template> templateContainer) {
        crud.setContainer(templateContainer);
    }

    public void addActionHandler(Action.Handler handler) {
        crud.addActionHandler(handler);
    }

    public void addCommitClickHandler(Button.ClickListener listener) {
        crud.getCommitButton().addClickListener(listener);
    }

    public void addDatasourceForProperty(String key,Container container) {
        crud.addDatasourceForProperty(key, container);
    }

    public Template getBean() {
        return crud.getBean();
    }

    public void editItem(BeanItem<Template> item) {
        crud.setItem(item);
    }

    public Item getCurrentItem() {
        return crud.getFieldGroup().getItemDataSource();

    }

    public void addCommandButtons(Collection<Command> commands) {
         crud.addCommandButtons(commands);;
    }
}
