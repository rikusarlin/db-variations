package fi.rikusarlin.db.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fi.rikusarlin.db.entity.Person;

@Repository 
public interface PersonRepository extends CrudRepository<Person, Integer> {
}