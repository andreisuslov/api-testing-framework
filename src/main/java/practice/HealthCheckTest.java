package practice;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;

public class HealthCheckTest {

    @Test
    public void healthCheckTest(){
        given().
        when().
            get("https://restful-booker.herokuapp.com/booking").
        then().
                // never trust a test that never fails
                // att first, you can put some random value
                // such as "249" and check the result
            assertThat().statusCode(200);
    }
}
