package de.gzockoll.prototype.templates.ui.components;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import de.gzockoll.prototype.templates.entity.Asset;

import java.io.InputStream;

import static java.lang.Math.min;

/**
 * Created by Guido.Zockoll on 06.03.2015.
 */
public class PreviewColumnGenerator implements Table.ColumnGenerator {

    public static final String MEDIA_PDF = "application/pdf";

    @Override
    public Component generateCell(Table table, Object itemId, Object columnId) {
        Asset asset= (Asset) itemId;
        switch (asset.getMimeType()) {
            case MEDIA_PDF:
                return createPreviewComponent(asset.asByteStream(), asset.getMimeType(),asset.getFilename());
            default:
                final String dataAsString = new String(asset.getData());
                return new Label(dataAsString.substring(0, min(dataAsString.length(),200)));
        }
    }

    private Component createPreviewComponent(InputStream stream, String mimeType, String filename) {
        Embedded component=new Embedded();
        component.setMimeType(mimeType);
        component.setType(Embedded.TYPE_BROWSER);
        component.setSource(new StreamResource(new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                return stream;
            }
        },filename));
        return component;
    }
}
