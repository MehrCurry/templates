package de.gzockoll.prototype.templates.entity;

import com.google.gwt.thirdparty.guava.common.io.ByteStreams;
import com.sun.istack.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.tika.Tika;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.io.IOException;
import java.io.InputStream;

@Entity
@EqualsAndHashCode @ToString @Getter
public class Asset extends AbstractEntity {
    @NotNull
    private String mimeType;
    @NotNull
    @Lob
    private byte[] data;
    private transient Tika tika=new Tika();

    private Asset() {}

    public Asset (byte[] data) {
        this.data=data;
        this.mimeType=tika.detect(data);
    }
    public Asset(InputStream is) {
        try {
            this.data=ByteStreams.toByteArray(is);
            this.mimeType=tika.detect(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
