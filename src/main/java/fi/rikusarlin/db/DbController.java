package fi.rikusarlin.db;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.db.entity.Person;
import fi.rikusarlin.db.repository.PersonRepository;

@RestController
public class DbController {

	@Autowired
	PersonRepository personRepository;
	
	@GetMapping("/persons")
	public ResponseEntity<List<Person>> getAllPersons() {
		
		try {
			List<Person> persons = new ArrayList<Person>();
			personRepository.findAll().forEach(persons::add);
			if (persons.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(persons, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

} 