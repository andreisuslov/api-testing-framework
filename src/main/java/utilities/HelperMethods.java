package utilities;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

public class HelperMethods {
    // the test often fails bc someone else is using that website
    // I created createBooking() method so if I want to
    // update or delete objects in the future, I can just create my own
    // object to make sure no one else is using it
    public static Response createBooking() {
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
        Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString())
                .post("https://restful-booker.herokuapp.com/booking");
        return response;
    }
}
