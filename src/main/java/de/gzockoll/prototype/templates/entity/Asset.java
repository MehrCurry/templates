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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Entity
@EqualsAndHashCode @ToString @Getter
public class Asset extends AbstractEntity {
    private static final Tika TIKA =new Tika();

    @NotNull
    private String mimeType;
    @NotNull
    @Lob
    private byte[] data;

    private String filename;

    public Asset() {}

    public Asset (byte[] data,String filename) {
        this.data=data;
        this.mimeType= TIKA.detect(data);
        this.filename=filename;
    }
    public Asset(InputStream is, String filename) {
        try {
            this.data=ByteStreams.toByteArray(is);
            this.mimeType= TIKA.detect(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Asset(String path) {
        try {
            final Path aPath = Paths.get(path);
            this.data= Files.readAllBytes(aPath);
            this.mimeType= TIKA.detect(data);
            this.filename=aPath.getFileName().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getSize() {
        return data != null ? data.length : 0L;
    }
}
