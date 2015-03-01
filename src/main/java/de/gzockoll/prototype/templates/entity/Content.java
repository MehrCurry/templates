package de.gzockoll.prototype.templates.entity;

import com.google.gwt.thirdparty.guava.common.io.ByteStreams;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.tika.Tika;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.stereotype.Controller;

import javax.persistence.Embeddable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;

@Embeddable @EqualsAndHashCode @ToString
public class Content {
    private String mimeType;
    private byte[] data;

    private Content() {}
    public Content(InputStream is) {
        Tika tika=new Tika();
        try {
            this.mimeType=tika.detect(is);
            data=ByteStreams.toByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
