package practice;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;

public class HealthCheckTest {

    @Test
    public void healthCheckTest(){
        // We need to create Request Specification variable using Request Spec Builder
        // to set up Base URI in order to avoid redundancy and improve re-usability.
        RequestSpecification rs =
                new RequestSpecBuilder().
                        setBaseUri("https://restful-booker.herokuapp.com").build();
        given().
                spec(rs).
        when().
            get("/ping").
        then().
                // never trust a test that never fails
                // att first, you can put some random value
                // such as "249" and check the result
            assertThat().statusCode(200);
    }
}
