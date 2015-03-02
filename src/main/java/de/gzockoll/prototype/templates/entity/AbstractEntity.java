package de.gzockoll.prototype.templates.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Version
    private long version;

    private Date createdAt = new Date();

    public Long getId() {
        return id;
    }


}
