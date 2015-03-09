package de.gzockoll.prototype.templates.entity;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Date;
import java.util.Set;

@MappedSuperclass
@Getter
public abstract class AbstractEntity extends ValidateableObject {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private long version;

    final private Date createdAt = new Date();
}
