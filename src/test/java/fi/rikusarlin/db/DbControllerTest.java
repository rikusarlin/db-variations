package fi.rikusarlin.db;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fi.rikusarlin.db.entity.Person;
import fi.rikusarlin.db.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
class DbControllerTest {

	@Mock
    PersonRepository mockPersonRepo;

    @InjectMocks
    DbController personService;
    
    @AfterEach
    public void tearDown() {
        clearInvocations(mockPersonRepo);
    }

	public static Person getPerson1() {
		Person person1 = new Person();
    	person1.setId(1);
    	person1.setFirstName("Rauli");
    	person1.setLastName("Wnape");
    	return person1;
	}
	
	public static Person getPerson2() {
		Person person2 = new Person();
    	person2.setId(2);
    	person2.setFirstName("Marke");
    	person2.setLastName("Peerakpe");
    	return person2;
	}

    @Test
    public void testFetchPersons_2found(){
    	List<Person> personList = new ArrayList<Person>();
    	personList.add(getPerson1());
    	personList.add(getPerson2());

    	when(mockPersonRepo.findAll()).thenReturn(personList);
    	
        ResponseEntity<List<Person>> result = personService.getAllPersons();

        Assertions.assertTrue(result.getStatusCode().equals(HttpStatus.OK));
        Assertions.assertTrue(result.getBody().size()==2);
    }

    @Test
    public void testFetchPersons_nonefound(){
    	List<Person> personList = new ArrayList<Person>();
    	when(mockPersonRepo.findAll()).thenReturn(personList);
    	
        ResponseEntity<List<Person>> result = personService.getAllPersons();

        Assertions.assertTrue(result.getStatusCode().equals(HttpStatus.NO_CONTENT));

    }

}