package practice;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import utilities.HelperMethods;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class DeleteBookingTests {

    @Test
    public void deleteBookingTests() {

        // Create JSON body
        Response responseCreate = HelperMethods.createBooking();
        responseCreate.prettyPrint();

        // Get bookingId of new booking
        int bookingid = responseCreate.jsonPath().getInt("bookingid");

        // Delete booking
        Response responseDelete = RestAssured.given().auth().preemptive().basic("admin", "password123")
                .delete("https://restful-booker.herokuapp.com/booking/" + bookingid);
        responseDelete.print();

        // Verifications
        // Verify response 201
        Assertions.assertEquals(201, responseDelete.getStatusCode(),  "Status code should be 201, but it's not.");

        Response responseGet = RestAssured.get("https://restful-booker.herokuapp.com/booking/" + bookingid);
        responseGet.print();

        Assertions.assertEquals("Not Found",responseGet.getBody().asString(),  "Body should be 'Not Found', but it's not.");
    }
}
