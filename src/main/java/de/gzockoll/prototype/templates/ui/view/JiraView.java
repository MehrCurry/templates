package de.gzockoll.prototype.templates.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vaadin.spring.annotation.VaadinUIScope;

import javax.annotation.PostConstruct;

@Component
@VaadinUIScope
@Getter
@Slf4j
public class JiraView extends CustomComponent implements View {
    private Grid grid;

    @PostConstruct
    public void init() {
        VerticalLayout main=new VerticalLayout();
        main.setSizeFull();
        this.grid=new Grid();
        main.addComponent(grid);
        setCompositionRoot(main);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
