package de.gzockoll.prototype.templates.ui.components;

import com.vaadin.server.StreamResource;

/**
 * Created by Guido.Zockoll on 06.03.2015.
 */
public interface OnDemandStreamSource extends StreamResource.StreamSource {
    String getFilename();
}
