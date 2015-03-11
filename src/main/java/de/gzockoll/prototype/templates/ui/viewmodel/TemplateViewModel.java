package de.gzockoll.prototype.templates.ui.viewmodel;

import com.google.common.collect.ImmutableSet;
import com.google.common.net.MediaType;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import de.gzockoll.prototype.templates.control.TemplateService;
import de.gzockoll.prototype.templates.entity.Asset;
import de.gzockoll.prototype.templates.entity.AssetRepository;
import de.gzockoll.prototype.templates.entity.Template;
import de.gzockoll.prototype.templates.entity.TemplateRepository;
import de.gzockoll.prototype.templates.ui.components.CommandAction;
import de.gzockoll.prototype.templates.ui.components.OnDemandStreamSource;
import de.gzockoll.prototype.templates.ui.view.TemplateView;
import de.gzockoll.prototype.templates.util.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.spring.annotation.VaadinUIScope;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import static de.gzockoll.prototype.templates.ui.util.ErrorHandler.handleError;

@Component
@VaadinUIScope
@Slf4j
public class TemplateViewModel implements View, OnDemandStreamSource, Action.Handler {
    private static final Action ACTION_ADD = new Action("Add");
    private static final Action ACTION_DELETE = new Action("Delete");
    private static final Action ACTION_PREVIEW = new Action("Preview");
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final String MIME_TYPE_PDF = MediaType.PDF.toString();
    public static final String MIME_TYPE_XSLT = "application/xslt+xml";
    public static final ImmutableSet<String> HIDDEN_FIELDS = ImmutableSet.of("id", "version", "valid", "approved");

    @Autowired
    private TemplateRepository repository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private TemplateView view;

    @Autowired
    private TemplateService service;

    private BeanItemContainer<Template> templateContainer = new BeanItemContainer<Template>(Template.class);
    private BeanItemContainer<Asset> xslContainer = new BeanItemContainer<Asset>(Asset.class);
    private BeanItemContainer<Asset> pdfContainer = new BeanItemContainer<Asset>(Asset.class);

    @PostConstruct
    public void init() {
        bind();
    }

    public void bind() {
        view.setViewChangeListener(this);
        templateContainer.removeAllItems();
        templateContainer.addAll(repository.findAll());
        HIDDEN_FIELDS.forEach(p -> templateContainer.removeContainerProperty(p));
        xslContainer.addAll(assetRepository.findByMimeType("application/xslt+xml"));
        pdfContainer.addAll(assetRepository.findByMimeType("application/pdf"));
        view.getStreamSource().setDelegate(this);
        view.setContainer(templateContainer);
        view.addActionHandler(this);
        view.addCommitClickHandler(e -> commit(e));
        view.addDatasourceForProperty("transform",xslContainer);
        view.addDatasourceForProperty("stationery", pdfContainer);
    }

    private void commit(Button.ClickEvent e) {
        try {
            view.commit();
            BeanItem<Template> item = (BeanItem<Template>) view.getCurrentItem();
            Template newBean = repository.save(item.getBean());
            refresh();
            view.setItem(templateContainer.getItem(newBean));
            view.addCommandButtons(service.getCommands(newBean));
        } catch (IllegalStateException ex) {
            handleError(ex);
        }
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
        templateContainer.addAll(repository.findAll());
        HIDDEN_FIELDS.forEach(p -> templateContainer.removeContainerProperty(p));
        xslContainer.removeAllItems();
        xslContainer.addAll(assetRepository.findByMimeType(MIME_TYPE_XSLT));
        pdfContainer.removeAllItems();
        pdfContainer.addAll(assetRepository.findByMimeType(MIME_TYPE_PDF));
    }

    @Override
    public String getFilename() {
        return "preview.pdf";
    }

    @Override
    public InputStream getStream() {
        Template t = view.getBean();
        byte[] data = service.preview((String) view.getEditor().getValue(), t.getStationery());
        return new ByteArrayInputStream(data);
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        return new Action[] {
                new CommandAction<Template>("Add", (s,t) -> {
                    Template justCreated = new Template();
                    final BeanItem<Template> item = getTemplateBeanItem(justCreated);
                    view.editItem(item);
                }),
                new CommandAction<Template>("Delete", (s,t) -> {
                    if (target!=null) {
                        repository.delete(t);
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
}
