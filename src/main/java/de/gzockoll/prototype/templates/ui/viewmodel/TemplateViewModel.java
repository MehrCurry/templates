package de.gzockoll.prototype.templates.ui.viewmodel;

import com.google.common.collect.ImmutableSet;
import com.google.common.net.MediaType;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import de.gzockoll.prototype.templates.control.TemplateService;
import de.gzockoll.prototype.templates.entity.Asset;
import de.gzockoll.prototype.templates.entity.AssetRepository;
import de.gzockoll.prototype.templates.entity.Template;
import de.gzockoll.prototype.templates.entity.TemplateRepository;
import de.gzockoll.prototype.templates.ui.components.OnDemandStreamSource;
import de.gzockoll.prototype.templates.ui.view.TemplateView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.spring.annotation.VaadinUIScope;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

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
    public static final ImmutableSet<String> HIDDEN_FIELDS = ImmutableSet.of("id", "version", "state","valid","approved");

    @Autowired
    private TemplateRepository repository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private TemplateView view;

    @Autowired
    private TemplateService service;

    private BeanItemContainer<Template> templateContainer = new BeanItemContainer<Template>(Template.class);
    private BeanItem<Template> templateItem = new BeanItem<>(new Template("de"));
    private BeanItemContainer<Asset> xslContainer = new BeanItemContainer<Asset>(Asset.class);
    private BeanItemContainer<Asset> pdfContainer = new BeanItemContainer<Asset>(Asset.class);

    @PostConstruct
    public void init() {
        bind();
    }

    public void refreshTable() {
        templateContainer.removeAllItems();
        templateContainer.addAll(repository.findAll());
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
            if (templateContainer.containsId(item)) {
                templateContainer.removeItem(item);
            }
            Template newBean = repository.save(item.getBean());
            templateContainer.addItem(getTemplateBeanItem(newBean));
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
        return new Action[] {ACTION_ADD,ACTION_DELETE, ACTION_PREVIEW};
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (ACTION_ADD==action) {
            Template justCreated = new Template();
            final BeanItem<Template> item = getTemplateBeanItem(justCreated);
            view.editItem(item);
        }
        if (ACTION_DELETE==action) {
            if (target!=null) {
                repository.delete((Template) target);
                templateContainer.removeItem(target);
            }
        }
    }

    private BeanItem<Template> getTemplateBeanItem(Template justCreated) {
        final BeanItem<Template> item = new BeanItem<Template>(justCreated);
        HIDDEN_FIELDS.forEach(f -> item.removeItemProperty(f));
        return item;
    }
}
