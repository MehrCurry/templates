package de.gzockoll.prototype.templates.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CustomComponent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vaadin.spring.annotation.VaadinUIScope;

@Component
@VaadinUIScope
@Getter
@Slf4j
public class EditView extends CustomComponent implements View {
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
