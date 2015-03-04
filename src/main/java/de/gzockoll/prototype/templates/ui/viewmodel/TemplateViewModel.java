package de.gzockoll.prototype.templates.ui.viewmodel;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.ui.Table;
import de.gzockoll.prototype.templates.control.TemplateService;
import de.gzockoll.prototype.templates.entity.Template;
import de.gzockoll.prototype.templates.entity.TemplateRepository;
import de.gzockoll.prototype.templates.ui.view.TemplateView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.spring.annotation.VaadinUIScope;

import javax.annotation.PostConstruct;

@Component
@VaadinUIScope
@Slf4j
public class TemplateViewModel {

    @Autowired
    private TemplateRepository repository;

    @Autowired
    private TemplateView view;

    @Autowired
    private TemplateService service;

    BeanItemContainer<Template> templateContainer =new BeanItemContainer<Template>(Template.class);
    BeanItem<Template> templateItem=new BeanItem<>(new Template("de"));

    @PostConstruct
    public void init() {
        bind();
    }

    public void refreshTable() {
        templateContainer.removeAllItems();
        templateContainer.addAll(repository.findAll());
    }

    public void bind() {
        final Table table = view.getTable();
        templateContainer.removeAllItems();
        templateContainer.addAll(repository.findAll());
        table.setContainerDataSource(templateContainer);
        table.addValueChangeListener(e -> log.debug("Event: " + e));
        table.setPageLength(20);
        table.setVisibleColumns(new Object[]{"id","state", "transform", "stationery", "createdAt"});
        view.setTemplateDataSource(templateItem);
        view.getSaveButton().addClickListener(clickEvent -> {
            try {
                BeanFieldGroup<Template> group = view.getGroup();
                group.commit();
                BeanItem<Template> beanItem = group.getItemDataSource();
                Template t=beanItem.getBean();
                repository.save(t);
            } catch (FieldGroup.CommitException e) {
                e.printStackTrace();
            }
        });
    }

    public View getView() {
        return view;
    }
}
