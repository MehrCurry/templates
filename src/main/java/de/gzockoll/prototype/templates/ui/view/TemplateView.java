package de.gzockoll.prototype.templates.ui.view;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import de.gzockoll.prototype.templates.entity.Template;
import de.gzockoll.prototype.templates.ui.components.OnDemandStreamSourceProxy;
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
    private Button previewButton =new Button("Preview");
    private Button saveButton=new Button("Save");
    private TextField language=new TextField("Language");
    private ComboBox transform=new ComboBox("XSL Transformer");
    private ComboBox stationery = new ComboBox("Stationery");
    private View viewChangeListener;
    private BeanFieldGroup<Template> group=new BeanFieldGroup<>(Template.class);
    private Embedded previewPDF=new Embedded();
    private TextArea editor=new TextArea();
    private OnDemandStreamSourceProxy streamSource=new OnDemandStreamSourceProxy();
    private BrowserWindowOpener browserWindowOpener=new BrowserWindowOpener(new StreamResource(streamSource,"preview.pdf"));

    @PostConstruct
    public void init() {
        HorizontalLayout mainLayout=new HorizontalLayout();
        mainLayout.setMargin(true);
        VerticalLayout left=new VerticalLayout();
        left.setSizeFull();

        table.setPageLength(6);
        table.setSelectable(true);
        left.addComponent(table);

        left.addComponent(createForm());

        left.addComponent(editor);
        editor.setImmediate(true);
        editor.setWidth("100%");
        editor.setHeight("200px");
        left.addComponent(previewButton);
        browserWindowOpener.extend(previewButton);

        mainLayout.addComponent(left);
        setCompositionRoot(mainLayout);
    }

    private Component createForm() {
        FormLayout form=new FormLayout();

        language.setImmediate(true);
        form.addComponent(language);
        form.addComponent(transform);
        transform.setItemCaptionPropertyId("filename");
        form.addComponent(stationery);
        stationery.setItemCaptionPropertyId("filename");
        group.bindMemberFields(this);
        form.addComponent(saveButton);
        return form;
    }

    public void setTemplateDataSource(Item item) {
        group.setItemDataSource(item);
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
