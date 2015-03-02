package de.gzockoll.prototype.templates.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@EqualsAndHashCode
@ToString
public class Tenant extends AbstractEntity {
    private String reference;
    private String memo;

    private Tenant() {
    }

    public Tenant(String reference, String memo) {
        this.reference = reference;
        this.memo = memo;
    }
}
