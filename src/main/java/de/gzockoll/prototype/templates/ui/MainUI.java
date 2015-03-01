package de.gzockoll.prototype.templates.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.vaadin.spring.annotation.VaadinUI;

@VaadinUI
public class MainUI extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new Label("Hi!"));
    }
}
