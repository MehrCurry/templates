package de.gzockoll.prototype.templates.entity;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;

@MappedSuperclass
@Getter
public abstract class AbstractEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Version
    private long version;

    final private Date createdAt = new Date();

}
