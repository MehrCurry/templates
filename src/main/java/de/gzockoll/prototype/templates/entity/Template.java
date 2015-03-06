package de.gzockoll.prototype.templates.entity;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import de.gzockoll.prototype.templates.validation.ValidISOLanguageCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.camel.ProducerTemplate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

@Entity @EqualsAndHashCode(callSuper = false) @ToString @Getter @Setter
public class Template extends AbstractEntity {

    @NotNull
    @ValidISOLanguageCode
    private String language;

    @OneToOne
    @NotNull
    private Asset transform;

    @ManyToOne
    @NotNull
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

    public byte[] generate(ProducerTemplate producer) {
        try {
            String data = new String(Files.readAllBytes(Paths.get("camel/vorlage/dataset.xml")), Charset.forName("UTF-8"));
            return generate(producer,data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] generate(ProducerTemplate producer, String data) {
        checkState(isApproved(),"This template is not approved for document generation");
            final Map<String, Object> headers = ImmutableMap.<String, Object>builder()
                    .put("templateId", getTransform().getId())
                    .put("stationeryId", getStationery().getId())
                    .build();
            return (byte[]) producer.requestBodyAndHeaders(data, headers);
    }
}
