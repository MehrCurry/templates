package de.gzockoll.prototype.templates.ui.components;

import com.vaadin.server.StreamResource;

import java.io.InputStream;

import static com.google.common.base.Preconditions.checkState;

/**
 * Created by Guido.Zockoll on 06.03.2015.
 */
public class OnDemandStreamSourceProxy implements StreamResource.StreamSource {
    private OnDemandStreamSource delegate;

    public void setDelegate(OnDemandStreamSource delegate) {
        this.delegate = delegate;
    }

    @Override
    public InputStream getStream() {
        checkState(delegate!=null);
        return delegate.getStream();
    }
}
