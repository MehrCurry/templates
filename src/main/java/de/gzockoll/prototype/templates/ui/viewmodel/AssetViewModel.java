package de.gzockoll.prototype.templates.ui.viewmodel;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import de.gzockoll.prototype.templates.entity.Asset;
import de.gzockoll.prototype.templates.entity.AssetRepository;
import de.gzockoll.prototype.templates.ui.view.AssetView;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Component
@Scope("prototype")
@Slf4j
@Getter
public class AssetViewModel {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetView assetView;

    BeanItemContainer<Asset> assetContainer = new BeanItemContainer<Asset>(Asset.class);
    private ByteArrayOutputStream data;

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
        assetTable.setVisibleColumns(new Object[]{"id", "filename", "mimeType", "size", "createdAt", "Links"});
        assetContainer.addItemSetChangeListener(event -> {
            ;
        });
        assetView.getUpload().setReceiver((filename, mimeType) -> {
            data = new ByteArrayOutputStream();
            return data;
        });
        assetView.getUpload().addFinishedListener(e -> {
            Asset a = new Asset(data.toByteArray(), e.getFilename());
            assetRepository.save(a);
            refreshTable();
        });
        assetView.setRemoveListener(e ->
                        log.debug(e.toString())
        );
        assetView.setPreviewListener(e ->
                        log.debug(e.toString())
        );
    }

    public void showPDF(byte[] data) {
        Window window = new Window();
        VerticalLayout layout = new VerticalLayout();

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
