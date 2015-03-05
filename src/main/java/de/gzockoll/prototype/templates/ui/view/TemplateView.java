package de.gzockoll.prototype.templates.ui.view;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import de.gzockoll.prototype.templates.entity.Template;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
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
    private Button saveButton=new Button("Save");
    private TextField language=new TextField("Language");
    private ComboBox transform=new ComboBox("XSL Transformer");
    private ComboBox stationery = new ComboBox("Stationery");
    private View viewChangeListener;
    private BeanFieldGroup<Template> group=new BeanFieldGroup<>(Template.class);
    private Embedded previewPDF=new Embedded();
    private AceEditor editor=new AceEditor();

    @PostConstruct
    public void init() {
        VerticalLayout layout=new VerticalLayout();
        layout.setHeightUndefined();
        layout.setWidth("100%");

        table.setPageLength(10);
        layout.addComponent(table);

        HorizontalLayout details=new HorizontalLayout();
        details.setSizeFull();
        details.setMargin(true);
        FormLayout left=new FormLayout();

        VerticalLayout right=new VerticalLayout();
        language.setImmediate(true);
        left.addComponent(language);
        left.addComponent(transform);
        transform.setItemCaptionPropertyId("filename");
        left.addComponent(stationery);
        stationery.setItemCaptionPropertyId("filename");
        group.bindMemberFields(this);
        left.addComponent(saveButton);
        details.addComponent(left);
        editor.setMode(AceMode.xml);
        details.addComponent(editor);

        right.addComponent(preview);
        previewPDF.setSizeFull();
        right.addComponent(previewPDF);
        details.addComponent(right);

        layout.addComponent(details);
        setCompositionRoot(layout);
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
