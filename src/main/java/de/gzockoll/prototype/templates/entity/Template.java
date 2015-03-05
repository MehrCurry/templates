package de.gzockoll.prototype.templates.entity;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity @EqualsAndHashCode(exclude = "id") @ToString @Getter @Setter
public class Template extends AbstractEntity {

    @NotNull
    @Size(min = 2,max = 2)
    private String language;

    @OneToOne
    private Asset transform;

    @OneToOne
    private Asset stationery;

    public Template() {
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
        return transform!=null && stationery !=null;
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

    public Template assignTransform(Asset a) {
        Preconditions.checkNotNull(a);
        state=state.assignTransform(this, a);
        return this;
    }

    public Template assignStationary(Asset a) {
        Preconditions.checkNotNull(a);
        state=state.assignStationary(this, a);
        return this;
    }

    void setTransform(Asset a) {
        Preconditions.checkNotNull(a);
        this.transform=a;
    }

    void setStationery(Asset a) {
        Preconditions.checkNotNull(a);
        this.stationery =a;
    }
}
