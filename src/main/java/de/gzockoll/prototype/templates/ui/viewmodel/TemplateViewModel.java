package de.gzockoll.prototype.templates.ui.viewmodel;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import de.gzockoll.prototype.templates.control.TemplateService;
import de.gzockoll.prototype.templates.entity.Asset;
import de.gzockoll.prototype.templates.entity.AssetRepository;
import de.gzockoll.prototype.templates.entity.Template;
import de.gzockoll.prototype.templates.entity.TemplateRepository;
import de.gzockoll.prototype.templates.ui.view.TemplateView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.spring.annotation.VaadinUIScope;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

@Component
@VaadinUIScope
@Slf4j
public class TemplateViewModel implements View {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    @Autowired
    private TemplateRepository repository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private TemplateView view;

    @Autowired
    private TemplateService service;

    private BeanItemContainer<Template> templateContainer =new BeanItemContainer<Template>(Template.class);
    private BeanItem<Template> templateItem=new BeanItem<>(new Template("de"));
    private BeanItemContainer<Asset> xslContainer=new BeanItemContainer<Asset>(Asset.class);
    private BeanItemContainer<Asset> pdfContainer=new BeanItemContainer<Asset>(Asset.class);

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
        final Table table = view.getTable();
        templateContainer.removeAllItems();
        templateContainer.addAll(repository.findAll());
        xslContainer.addAll(assetRepository.findByMimeType("application/xslt+xml"));
        pdfContainer.addAll(assetRepository.findByMimeType("application/pdf"));
        view.getTransform().setContainerDataSource(xslContainer);
        view.getStationery().setContainerDataSource(pdfContainer);
        table.setContainerDataSource(templateContainer);
        table.addValueChangeListener(e -> log.debug("Event: " + e));
        table.setPageLength(20);
        table.setVisibleColumns(new Object[]{"id","state", "transform", "stationery", "createdAt"});
        view.setTemplateDataSource(templateItem);
        view.getSaveButton().addClickListener(clickEvent -> {
            try {
                view.getGroup().commit();
                BeanItem<Template> beanItem = view.getGroup().getItemDataSource();
                Template t=beanItem.getBean();
                repository.save(t);
                view.getEditor().setValue(new String(t.getTransform().getData(), UTF_8));
                refreshTable();
            } catch (FieldGroup.CommitException e) {
                e.printStackTrace();
            }
        });
        view.getPreview().addClickListener(clickEvent -> {
            BeanItem<Template> beanItem = view.getGroup().getItemDataSource();
            Template t=beanItem.getBean();
            byte[] data = service.preview(t);

            final Embedded previewPDF = view.getPreviewPDF();
            previewPDF.setSource(new StreamResource(() -> {
                return new ByteArrayInputStream(data);
            }, "file.pdf"));

            previewPDF.setType(Embedded.TYPE_BROWSER);
            previewPDF.setMimeType("application/pdf");
            previewPDF.setSizeFull();
        });
        view.getTransform().addValueChangeListener(event -> view.getEditor().setValue(event.toString()));
        view.getEditor().addValueChangeListener(event -> {
            Template t = templateItem.getBean();
            // t.getTransform().updateData(event.getProperty().getValue());
        });
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
        xslContainer.removeAllItems();
        xslContainer.addAll(assetRepository.findByMimeType("application/xslt+xml"));
        pdfContainer.removeAllItems();
        pdfContainer.addAll(assetRepository.findByMimeType("application/pdf"));
    }
}
