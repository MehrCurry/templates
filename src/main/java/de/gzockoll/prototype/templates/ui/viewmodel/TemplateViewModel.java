package de.gzockoll.prototype.templates.ui.viewmodel;

import com.google.common.collect.ImmutableSet;
import com.google.common.net.MediaType;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.event.SelectionEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import de.gzockoll.prototype.templates.control.AssetService;
import de.gzockoll.prototype.templates.control.TemplateService;
import de.gzockoll.prototype.templates.entity.Asset;
import de.gzockoll.prototype.templates.entity.Template;
import de.gzockoll.prototype.templates.ui.components.CommandAction;
import de.gzockoll.prototype.templates.ui.view.TemplateView;
import de.gzockoll.prototype.templates.util.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.spring.annotation.VaadinUIScope;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Optional;

import static de.gzockoll.prototype.templates.ui.util.ErrorHandler.handleError;

@Component
@VaadinUIScope
@Slf4j
public class TemplateViewModel implements View, Action.Handler, SelectionEvent.SelectionListener {
    private static final Action ACTION_ADD = new Action("Add");
    private static final Action ACTION_DELETE = new Action("Delete");
    private static final Action ACTION_PREVIEW = new Action("Preview");
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final String MIME_TYPE_PDF = MediaType.PDF.toString();
    public static final String MIME_TYPE_XSLT = "application/xslt+xml";
    public static final ImmutableSet<String> HIDDEN_FIELDS = ImmutableSet.of("id", "version", "valid", "approved");

    @Autowired
    private TemplateView view;

    @Autowired
    private TemplateService service;

    @Autowired
    private AssetService assetService;

    private BeanItemContainer<Template> templateContainer = new BeanItemContainer<>(Template.class);
    private BeanItemContainer<Asset> xslContainer = new BeanItemContainer<>(Asset.class);
    private BeanItemContainer<Asset> pdfContainer = new BeanItemContainer<>(Asset.class);

    private BeanItem<Template> selectedTemplate=new BeanItem<Template>(new Template());
    @PostConstruct
    public void init() {
        bind();
    }

    public void bind() {
        view.setViewChangeListener(this);
        HIDDEN_FIELDS.forEach(f -> view.getCrud().getGrid().removeColumn(f));
        templateContainer.removeAllItems();
        templateContainer.addAll(service.findAllTemplates());
        HIDDEN_FIELDS.forEach(p -> templateContainer.removeContainerProperty(p));
        xslContainer.addAll(assetService.findByMimeType("application/xslt+xml"));
        pdfContainer.addAll(assetService.findByMimeType("application/pdf"));
        view.getCrud().setContainer(templateContainer);
        // view.getCrud().addActionHandler(this);
        view.getCrud().getCommitButton().addClickListener(e -> commit(e));
        view.getCrud().getGrid().addSelectionListener(this);
        view.getCrud().addDatasourceForProperty("transform", xslContainer);
        view.getCrud().addDatasourceForProperty("stationery", pdfContainer);
        view.getCrud().getAddButton().addClickListener(e -> selectTemplate(new Template()));
        view.getCrud().getDeleteButton().addClickListener(e -> { service.delete(selectedTemplate.getBean()); refresh();});
        view.getPreviewButton().addClickListener(e -> showPDF(service.preview(selectedTemplate.getBean())));
    }

    private void commit(Button.ClickEvent e) {
        try {
            view.getCrud().commit();
            Template newBean = service.save(selectedTemplate.getBean());
            refresh();
            selectTemplate(new Template());
            view.addCommandButtons(service.getCommands(newBean));
        } catch (IllegalStateException ex) {
            handleError(ex);
        }
    }

    private void selectTemplate(Template template) {
        selectedTemplate=new BeanItem<Template>(template);
        HIDDEN_FIELDS.forEach(f -> selectedTemplate.removeItemProperty(f));
        view.getCrud().setItem(selectedTemplate);
    }

    public View getView() {
        return view;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        refresh();
    }

    private void refresh() {
        templateContainer.removeAllItems();
        templateContainer.addAll(service.findAllTemplates());
        HIDDEN_FIELDS.forEach(p -> templateContainer.removeContainerProperty(p));
        xslContainer.removeAllItems();
        xslContainer.addAll(assetService.findByMimeType(MIME_TYPE_XSLT));
        pdfContainer.removeAllItems();
        pdfContainer.addAll(assetService.findByMimeType(MIME_TYPE_PDF));
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        return new Action[] {
                new CommandAction<Template>("Add", (s,t) -> {
                    Template justCreated = new Template();
                    selectTemplate(justCreated);
                }),
                new CommandAction<Template>("Delete", (s,t) -> {
                    if (target!=null) {
                        service.delete(t);
                        templateContainer.removeItem(t);
                    }
                }),
                new CommandAction<Template>("Preview", (s,t) -> {
                    showPDF(service.preview(t));
                })};
    }

    @Override
    public void handleAction(Action a, Object sender, Object target) {
        CommandAction action= (CommandAction) a;
        action.handle(sender, target);
        refresh();
    }

    private BeanItem<Template> getTemplateBeanItem(Template justCreated) {
        final BeanItem<Template> item = new BeanItem<Template>(justCreated);
        HIDDEN_FIELDS.forEach(f -> item.removeItemProperty(f));
        Collection<Command> commands = service.getCommands(justCreated);
        return item;
    }

    private void showPDF(byte[] data) {
        Resource resource=new StreamResource(() -> new ByteArrayInputStream(data),"preview.pdf");
        BrowserFrame frame=new BrowserFrame("Preview",resource);
        frame.setSizeFull();
        Window subWindow=new Window("Preview");
        subWindow.setWidth("50%");
        subWindow.setHeight("100%");
        subWindow.setContent(frame);
        UI.getCurrent().addWindow(subWindow);
    }


    @Override
    public void select(SelectionEvent event) {
        final Optional<Object> selected = event.getSelected().stream().findFirst();
        selected.ifPresent(o -> selectTemplate(templateContainer.getItem(o).getBean()));
        view.getCrud().getDeleteButton().setEnabled(selected.isPresent());
    }
}
