package de.gzockoll.prototype.templates.ui.viewmodel;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import de.gzockoll.prototype.templates.control.JiraConnector;
import de.gzockoll.prototype.templates.entity.JiraTicket;
import de.gzockoll.prototype.templates.ui.view.JiraView;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Scope("prototype")
@Slf4j
@Getter
public class JiraViewModel implements View {

    @Autowired
    private JiraView view;

    @Autowired
    private JiraConnector connector;

    private BeanItemContainer<JiraTicket> container = new BeanItemContainer<>(JiraTicket.class);

    @PostConstruct
    public void bind() {
        container.addAll(connector.findAll());
        view.getGrid().setContainerDataSource(container);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        view.enter(viewChangeEvent);
    }
}
