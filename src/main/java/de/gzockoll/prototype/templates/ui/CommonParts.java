package de.gzockoll.prototype.templates.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class CommonParts extends VerticalLayout implements View,Upload.Receiver {

    private ByteArrayOutputStream data;

    public CommonParts() {
        setMargin(true);
        addStyleName("content-common");

        Label h1 = new Label("Common UI Elements");
        h1.addStyleName("h1");
        addComponent(h1);

        Upload upload = new Upload("Upload it here", this);
        upload.addFinishedListener(e ->
                log.debug("Finished" + e));
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
