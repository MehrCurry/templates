package de.gzockoll.prototype.templates.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class CommonParts extends VerticalLayout implements View {

    public CommonParts() {
        setMargin(true);
        addStyleName("content-common");

        Label h1 = new Label("Common UI Elements");
        h1.addStyleName("h1");
        addComponent(h1);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
