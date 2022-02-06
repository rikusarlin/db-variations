package fi.rikusarlin.db.service;

import org.springframework.stereotype.Component;

import fi.rikusarlin.db.entity.Person;
import fi.rikusarlin.db.repository.PersonRepository;
import reactor.core.publisher.Flux;

@Component
public class PersonServer {

	
	private final PersonRepository personRepository;

	public PersonServer(PersonRepository repository) {
		this.personRepository = repository;
	}
	
	public Flux<Person> findAllPersons() {
		return personRepository.findAll();
	}

}
