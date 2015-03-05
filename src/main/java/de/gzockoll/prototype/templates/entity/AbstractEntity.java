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
public abstract class AbstractEntity {
    private static final Validator VALIDATOR= Validation.buildDefaultValidatorFactory().getValidator();

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private long version;

    final private Date createdAt = new Date();

    public Set<ConstraintViolation<AbstractEntity>> getValidationErrors() {
        return VALIDATOR.validate(this);
    }

    public boolean isValid() {
        return getValidationErrors().size()==0;
    }

    public void validate() throws ConstraintViolationException {
        Set<ConstraintViolation<AbstractEntity>> errors = getValidationErrors();
        if (errors.size()>0) {
            throw new ConstraintViolationException("Validation",errors);
        }

    }


}
