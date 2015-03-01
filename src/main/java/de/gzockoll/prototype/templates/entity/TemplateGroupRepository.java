package de.gzockoll.prototype.templates.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateGroupRepository extends CrudRepository<TemplateGroup, TemplateGroupPK> {
}
