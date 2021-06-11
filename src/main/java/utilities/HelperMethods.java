package utilities;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.Before;

public class HelperMethods {

    protected RequestSpecification rs;

    // the test often fails bc someone else is using that website
    // I created createBooking() method so if I want to
    // update or delete objects in the future, I can just create my own
    // object to make sure no one else is using it
    protected Response createBooking() {
        // Create JSON body
        JSONObject body = new JSONObject();
        body.put("firstname", "Andrei");
        body.put("lastname", "Suslov");
        body.put("totalprice", 150);
        body.put("depositpaid", false);

        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2021-06-03");
        bookingdates.put("checkout", "2021-06-10");
        body.put("bookingdates", bookingdates);
        body.put("additionalneeds", "Swimming Pool");

        // Get the response
        Response response = RestAssured.given(rs).contentType(ContentType.JSON).body(body.toString())
                .post("/booking");
        return response;
    }

    @Before
    protected void setUp() {
        rs = new RequestSpecBuilder().
                setBaseUri("https://restful-booker.herokuapp.com").build();
    }
}
