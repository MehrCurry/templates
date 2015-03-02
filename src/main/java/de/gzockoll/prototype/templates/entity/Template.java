package de.gzockoll.prototype.templates.entity;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Entity @EqualsAndHashCode(exclude = "id") @ToString
public class Template extends AbstractEntity {

    @NotNull
    @Size(min = 2,max = 2)
    private String language;

    @OneToOne
    private Asset transform;

    @OneToOne
    private Asset stationary;

    private Template() {
    }

    public Template(@Size(min = 2, max = 2) String language) {
        this.language = language;
    }

    private TemplateState state=TemplateState.EDITABLE;

    public Template requestApproval() {
        Preconditions.checkState(assetsPresent());
        state=state.requestApproval();
        return this;
    }

    private boolean assetsPresent() {
        return transform!=null && stationary!=null;
    }

    public boolean isApproved() {
        return state==TemplateState.APPROVED;
    }

    public Template approve() {
        Preconditions.checkState(assetsPresent());
        state=state.approve();
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public Template saveContent(InputStream is) {
        Preconditions.checkNotNull(is);
        state=state.saveContent(this,is);
        return this;
    }

    void setTransform(InputStream input) {
        final Asset asset = new Asset(input);
        Preconditions.checkArgument(asset.getMimeType().equals("application/xml"),"Transformer script must be a xsl file");
        transform= asset;
    }

    void setStationary(InputStream input) {
        final Asset asset = new Asset(input);
        Preconditions.checkArgument(asset.getMimeType().equals("application/pdf"),"Stationary must be a pdf file");
        stationary=new Asset(input);
    }

    public Template saveContent(String s) {
        return saveContent(new ByteArrayInputStream(s.getBytes()));
    }
}
