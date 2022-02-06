package fi.rikusarlin.db.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Component;

import fi.rikusarlin.db.entity.Person;

@Component
public interface PersonRepository extends R2dbcRepository<Person, UUID> {
}