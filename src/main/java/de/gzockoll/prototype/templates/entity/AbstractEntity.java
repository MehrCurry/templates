package de.gzockoll.prototype.templates.entity;

import com.google.common.collect.ImmutableList;
import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;
import java.util.List;

@MappedSuperclass
@Getter
public abstract class AbstractEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Version
    private long version;

    final private Date createdAt = new Date();

    public List<String> getHiddenAttributes() {
        return ImmutableList.of("hiddenAttributes", "id", "version", "createdAt");
    }
}
