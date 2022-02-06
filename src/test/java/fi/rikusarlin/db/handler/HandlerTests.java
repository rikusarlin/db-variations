package fi.rikusarlin.db.handler;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import fi.rikusarlin.db.entity.Person;
import fi.rikusarlin.db.router.Router;
import fi.rikusarlin.db.service.PersonServer;
import fi.rikusarlin.db.testdata.PersonData;
import reactor.core.publisher.Flux;

@RunWith(SpringRunner.class)
@WebFluxTest(PersonHandler.class)
@Import(Router.class)
public class HandlerTests 
{
    @MockBean
    PersonServer personServer;
    
    @Autowired
    private WebTestClient webClient; 

    @Test
    void testGetAll() {
		Person[] persons = new Person[2];
		persons[0] = PersonData.getPerson1();
		persons[1] = PersonData.getPerson2();

        Mockito.when(personServer.findAllPersons()).thenReturn(Flux.fromArray(persons));
        this.webClient.get()
            .uri("/persons")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.length()").isEqualTo(2);
        Mockito.verify(personServer, times(1)).findAllPersons();
    }
}