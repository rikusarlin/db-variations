package fi.rikusarlin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class PersonResourceTest {

    @Test
    public void testPersonEndpoint() {
        given()
          .when().get("/persons")
          .then()
             .statusCode(200)
             .body("size()", is(4));
    }
}