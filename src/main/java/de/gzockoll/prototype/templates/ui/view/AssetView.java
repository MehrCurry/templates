package de.gzockoll.prototype.templates.ui.view;

import com.vaadin.ui.*;
import de.gzockoll.prototype.templates.entity.Asset;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vaadin.spring.annotation.VaadinUIScope;

import javax.annotation.PostConstruct;

@Component
@VaadinUIScope
@Getter
@Slf4j
public class AssetView extends CustomComponent {
    Table assetTable=new Table();
    Button preview=new Button("Preview");
    Label time=new Label();
    CRUDForm<Asset> crudForm=new CRUDForm<>(Asset.class,"id","version","createdAt","data");

    @PostConstruct
    public void init() {
        VerticalLayout layout=new VerticalLayout();
        layout.setSizeFull();
        layout.addComponent(preview);
        layout.addComponent(time);
        assetTable.addGeneratedColumn("Remove",new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object itemId, Object columnId) {
                Button removeButton = new Button("x");
                removeButton.addClickListener(e -> table.removeItem(itemId));
                return removeButton;
            }
        });

        layout.addComponent(assetTable);
        layout.addComponent(crudForm);

        setCompositionRoot(layout);
    }
}
