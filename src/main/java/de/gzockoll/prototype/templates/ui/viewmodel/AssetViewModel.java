package de.gzockoll.prototype.templates.ui.viewmodel;

import com.google.common.base.Stopwatch;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import de.gzockoll.prototype.templates.control.TemplateService;
import de.gzockoll.prototype.templates.entity.Asset;
import de.gzockoll.prototype.templates.entity.AssetRepository;
import de.gzockoll.prototype.templates.entity.Template;
import de.gzockoll.prototype.templates.ui.view.AssetView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
@Scope("prototype")
@Slf4j
public class AssetViewModel {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetView assetView;

    @Autowired
    private TemplateService service;

    BeanItemContainer<Asset> assetContainer=new BeanItemContainer<Asset>(Asset.class);

    @PostConstruct
    public void init() {
        bind();
    }

    public void refreshTable() {
        assetContainer.removeAllItems();
        assetContainer.addAll(assetRepository.findAll());
    }

    public void bind() {
        final Table assetTable = assetView.getAssetTable();
        assetContainer.removeAllItems();
        assetContainer.addAll(assetRepository.findAll());
        assetTable.setContainerDataSource(assetContainer);
        assetTable.addValueChangeListener(e -> log.debug("Event: " + e));
        assetTable.setPageLength(20);
        assetTable.setVisibleColumns(new Object[]{"id","filename","mimeType","size", "createdAt"});

        assetView.getPreview().addClickListener(e -> {
                    Template forPreview = new Template("de")
                            .assignTransform(assetRepository.findOne(1L))
                            .assignStationary(assetRepository.findOne(2L));
                    try {
                        Stopwatch sw=Stopwatch.createStarted();
                        byte[] data = service.preview(forPreview);
                        sw.stop();
                        assetView.getTime().setValue(sw.toString());
                        showPDF(data);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
        );
        assetView.getCrudForm().setBeanItemContainerDataSource(assetContainer);
    }
    public void showPDF(byte[] data) {
        Window window = new Window();
        VerticalLayout layout=new VerticalLayout();

        Embedded pdf = new Embedded("Preview", new StreamResource(() -> {
            return new ByteArrayInputStream(data);
        }, "file.pdf"));

        pdf.setType(Embedded.TYPE_BROWSER);
        pdf.setMimeType("application/pdf");
        pdf.setSizeFull();
        layout.addComponent(pdf);
        layout.setSizeFull();

        window.setContent(layout);
        window.setSizeFull();
        UI.getCurrent().addWindow(window);
    }

    public CustomComponent getView() {
        return assetView;
    }

    private void reloadTable() {
        // templateContainer.refresh();
        assetView.getAssetTable().setCurrentPageFirstItemId(0);
    }

}
