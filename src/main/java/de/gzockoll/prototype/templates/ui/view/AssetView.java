package de.gzockoll.prototype.templates.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vaadin.spring.annotation.VaadinUIScope;

import javax.annotation.PostConstruct;

@Component
@VaadinUIScope
@Getter
@Slf4j
public class AssetView extends CustomComponent implements View {
    private Table assetTable=new Table();
    private Label time=new Label();
    private Upload upload = new Upload("Upload it here",null);
    private Button.ClickListener removeListener;
    private Button.ClickListener previewListener;

    public void setRemoveListener(Button.ClickListener removeListener) {
        this.removeListener = removeListener;
    }

    public void setPreviewListener(Button.ClickListener previewListener) {
        this.previewListener = previewListener;
    }

    @PostConstruct
    public void init() {
        VerticalLayout layout=new VerticalLayout();
        layout.setMargin(true);
        layout.addComponent(time);
        layout.addComponent(upload);
        assetTable.addGeneratedColumn("Links", (table, itemId, columnId) -> {
            HorizontalLayout links=new HorizontalLayout();
            Button previewButton = new Button("Preview");
            previewButton.setStyleName(ValoTheme.BUTTON_LINK);
            previewButton.addClickListener(e -> {
                table.select(itemId);
                if (previewListener != null)
                    previewListener.buttonClick(e);
            });
            links.addComponent(previewButton);
            Button removeButton = new Button("Delete");
            removeButton.setStyleName(ValoTheme.BUTTON_LINK);
            removeButton.addClickListener(e -> {
                table.select(itemId);
                if (removeListener != null) {
                    removeListener.buttonClick(e);
                }
            });
            links.addComponent(removeButton);
            return links;
        });
        assetTable.setColumnHeader("Remove","");

        layout.addComponent(assetTable);
        setCompositionRoot(layout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
