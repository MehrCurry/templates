package de.gzockoll.prototype.templates.ui.viewmodel;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import de.gzockoll.prototype.templates.control.TemplateService;
import de.gzockoll.prototype.templates.entity.Asset;
import de.gzockoll.prototype.templates.entity.AssetRepository;
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
public class TemplateViewModel implements View {

    @Autowired
    private TemplateRepository repository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private TemplateView view;

    @Autowired
    private TemplateService service;

    BeanItemContainer<Template> templateContainer =new BeanItemContainer<Template>(Template.class);
    BeanItem<Template> templateItem=new BeanItem<>(new Template("de"));
    BeanItemContainer<Asset> xslContainer=new BeanItemContainer<Asset>(Asset.class);
    BeanItemContainer<Asset> pdfContainer=new BeanItemContainer<Asset>(Asset.class);

    @PostConstruct
    public void init() {
        bind();
    }

    public void refreshTable() {
        templateContainer.removeAllItems();
        templateContainer.addAll(repository.findAll());
    }

    public void bind() {
        view.setViewChangeListener(this);
        final Table table = view.getTable();
        templateContainer.removeAllItems();
        templateContainer.addAll(repository.findAll());
        xslContainer.addAll(assetRepository.findByMimeType("application/xslt+xml"));
        pdfContainer.addAll(assetRepository.findByMimeType("application/pdf"));
        view.getTemplates().setContainerDataSource(xslContainer);
        view.getStationery().setContainerDataSource(pdfContainer);
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

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        bind();
    }
}
