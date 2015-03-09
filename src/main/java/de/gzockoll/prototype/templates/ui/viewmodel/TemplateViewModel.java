package de.gzockoll.prototype.templates.ui.viewmodel;

import com.google.common.net.MediaType;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Table;
import de.gzockoll.prototype.templates.control.TemplateService;
import de.gzockoll.prototype.templates.entity.Asset;
import de.gzockoll.prototype.templates.entity.AssetRepository;
import de.gzockoll.prototype.templates.entity.Template;
import de.gzockoll.prototype.templates.entity.TemplateRepository;
import de.gzockoll.prototype.templates.ui.components.OnDemandStreamSource;
import de.gzockoll.prototype.templates.ui.view.TemplateView;
import de.gzockoll.prototype.templates.validation.XMLValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.spring.annotation.VaadinUIScope;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

import static java.lang.StrictMath.min;

@Component
@VaadinUIScope
@Slf4j
public class TemplateViewModel implements View, OnDemandStreamSource {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final String MIME_TYPE_PDF = MediaType.PDF.toString();
    public static final String MIME_TYPE_XSLT = "application/xslt+xml";

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
        final Table table = view.getTable();
        templateContainer.removeAllItems();
        templateContainer.addAll(repository.findAll());
        xslContainer.addAll(assetRepository.findByMimeType("application/xslt+xml"));
        pdfContainer.addAll(assetRepository.findByMimeType("application/pdf"));
        view.getTransform().setContainerDataSource(xslContainer);
        view.getStationery().setContainerDataSource(pdfContainer);
        table.setContainerDataSource(templateContainer);
        table.addValueChangeListener(e -> log.debug("Event: " + e));
        table.setPageLength(min(6, table.size()));
        table.setVisibleColumns(new Object[]{"id", "state", "transform", "stationery", "createdAt"});
        view.setTemplateDataSource(templateItem);
        view.getSaveButton().addClickListener(clickEvent -> {
            try {
                view.getGroup().commit();
                BeanItem<Template> beanItem = view.getGroup().getItemDataSource();
                Template t = beanItem.getBean();
                Template updatedVersion=repository.save(t);
                templateItem=new BeanItem<Template>(updatedVersion);
                view.getGroup().setItemDataSource(templateItem);
                view.getEditor().setValue(new String(t.getTransform().getData(), UTF_8));
                refreshTable();
            } catch (FieldGroup.CommitException e) {
                e.printStackTrace();
            }
        });
        view.getStreamSource().setDelegate(this);
        table.addItemClickListener(e -> {
            BeanItem item = (BeanItem) e.getItem();
            view.getGroup().setItemDataSource(item);
        });
        view.getTransform().addValueChangeListener(e-> {
            Asset asset= (Asset) e.getProperty().getValue();
            if (asset!=null) {
                view.getEditor().setValue(new String(asset.getData()));
            }
        });
        view.getNewButton().addClickListener(e-> {
            view.getEditor().setValue("");
            view.getGroup().setItemDataSource(new Template(Locale.getDefault().getLanguage()));
        });
        // view.getEditor().addValidator(new XMLValidator("http://www.w3.org/2007/schema-for-xslt20.xsd","http://svn.apache.org/repos/asf/xmlgraphics/fop/trunk/src/foschema/fop.xsd"));
        view.getBinder().setBeanDataSource(new Template());
        view.getCrud().setContainer(templateContainer);
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
        Template t = view.getGroup().getItemDataSource().getBean();
        byte[] data = service.preview((String) view.getEditor().getValue(), t.getStationery());
        return new ByteArrayInputStream(data);
    }
}
