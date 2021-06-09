package practice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import utilities.HelperMethods;

public class PartialUpdateBookingTests {

    @Test
    public void updateBookingTests() {

        // Create JSON body
        Response responseCreate = HelperMethods.createBooking();
        responseCreate.prettyPrint();

        JSONObject body = new JSONObject();
        body.put("firstname", "Irina");

        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2021-06-01");
        body.put("bookingdates", bookingdates);

        // get booking id of the new booking
        int bookingId = responseCreate.jsonPath().getInt("bookingid");

        // Get the response using booking ID
        Response responseUpdate = RestAssured.given()
                .auth().preemptive().basic("admin", "password123")
                .contentType(ContentType.JSON).body(body.toString())
                .patch("https://restful-booker.herokuapp.com/booking/" + bookingId);

        // Verify All fields
        int expected = 200;
        int actual = responseUpdate.getStatusCode();

        Assertions.assertEquals(expected, actual, "Status code is supposed to be 200, but it is " + actual);

        System.out.println("The actual status code is " + actual + ", and it's supposed to be " + expected + ".");

        String actualFirstName = responseUpdate.jsonPath().getString("firstname");
        String expectedFirstName = "Irina";
        Assertions.assertEquals(expectedFirstName, actualFirstName, "The name in the response is "
                + actualFirstName + ", and it's supposed to be " + expectedFirstName);

        String actualCheckIn = responseUpdate.jsonPath().getString("bookingdates.checkin");
        String expectedCheckIn = "2021-06-01";
        Assertions.assertEquals(expectedCheckIn, actualCheckIn, "checkin in response is not expected");


        responseUpdate.prettyPrint();
    }
}



