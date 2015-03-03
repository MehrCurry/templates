package de.gzockoll.prototype.templates.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import de.gzockoll.prototype.templates.control.AssetController;
import de.gzockoll.prototype.templates.entity.Asset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@Component
@Scope("prototype")
@Slf4j
public class CommonParts extends VerticalLayout implements View,Upload.Receiver {

    @Autowired
    private AssetController controller;

    private ByteArrayOutputStream data;

    public CommonParts() {
        setMargin(true);
        addStyleName("content-common");

        Label h1 = new Label("Common UI Elements");
        h1.addStyleName("h1");
        addComponent(h1);

        Upload upload = new Upload("Upload it here", this);
        upload.addFinishedListener(e -> {
                    Asset a = new Asset(data.toByteArray(),e.getFilename());
                    controller.save(a);
                });
                addComponent(upload);
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
