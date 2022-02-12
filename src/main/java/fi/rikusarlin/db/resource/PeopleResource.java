package fi.rikusarlin.db.resource;

import fi.rikusarlin.db.entity.Person;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "persons")
public interface PeopleResource extends PanacheEntityResource<Person, Long> {
}