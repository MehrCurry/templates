package de.gzockoll.prototype.templates.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import de.gzockoll.prototype.templates.control.AssetController;
import de.gzockoll.prototype.templates.entity.Asset;
import de.gzockoll.prototype.templates.ui.viewmodel.AssetViewModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@VaadinUIScope
@Slf4j
@VaadinView(name = "commonParts")
public class CommonParts extends VerticalLayout implements View,Upload.Receiver {

    @Autowired
    private AssetController controller;

    @Autowired
    private AssetViewModel viewModel;

    private ByteArrayOutputStream data;

    @PostConstruct
    public void init() {
        setMargin(true);
        addStyleName("content-common");

        Label h1 = new Label("Common UI Elements");
        h1.addStyleName("h1");
        addComponent(h1);

        Upload upload = new Upload("Upload it here", this);
        upload.addFinishedListener(e -> {
                    Asset a = new Asset(data.toByteArray(),e.getFilename());
                    controller.save(a);
                    viewModel.refreshTable();
                });
        addComponent(upload);
        addComponent(viewModel.getView());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        data=new ByteArrayOutputStream();
        return data;
    }
}
