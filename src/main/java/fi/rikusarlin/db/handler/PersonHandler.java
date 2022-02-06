package fi.rikusarlin.db.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import fi.rikusarlin.db.service.PersonServer;
import reactor.core.publisher.Mono;

@Component
@Controller
public class PersonHandler {

	static Logger logger = LoggerFactory.getLogger(PersonHandler.class);

	private MediaType json = MediaType.APPLICATION_JSON;

	private final PersonServer personServer;

	public PersonHandler(PersonServer server) {
		this.personServer = server;
	}
	
	public Mono<ServerResponse> getAll(ServerRequest request) {
		return personServer.findAllPersons().collectList()
				.doOnEach(person -> logger.debug(person.toString()))
				.flatMap(list -> ServerResponse.ok().contentType(json).bodyValue(list));
	}

}
