package practice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import utilities.HelperMethods;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UpdateBookingTests extends HelperMethods{

    @Test
    public void updateBookingTests() {

        // Create JSON body
        Response responseCreate = createBooking();
        responseCreate.print();

        JSONObject body = new JSONObject();
        body.put("firstname", "Irina");
        body.put("lastname", "Suslov");
        body.put("totalprice", 150);
        body.put("depositpaid", false);

        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2021-06-03");
        bookingdates.put("checkout", "2021-06-10");
        body.put("bookingdates", bookingdates);
        body.put("additionalneeds", "Swimming Pool");

        // get booking id of the new booking
        int bookingId = responseCreate.jsonPath().getInt("bookingid");

        // Get the response using booking ID
        Response responseUpdate = RestAssured.given(rs)
                .auth().preemptive().basic("admin", "password123")
                .contentType(ContentType.JSON).body(body.toString())
                .put("/booking/" + bookingId);

        // Verify All fields
        int expected = 200;
        int actual = responseUpdate.getStatusCode();

        Assertions.assertEquals(expected, actual, "Status code is supposed to be 200, but it is " + actual);

        System.out.println("The actual status code is " + actual + ", and it's supposed to be " + expected + ".");

        String actualFirstName = responseUpdate.jsonPath().getString("firstname");
        String expectedFirstName = "Irina";
        Assertions.assertEquals(expectedFirstName, actualFirstName, "The name in the response is "
                + actualFirstName + ", and it's supposed to be " + expectedFirstName);

        String actualLastName = responseUpdate.jsonPath().getString("lastname");
        String expectedLastName = "Suslov";
        Assertions.assertEquals(expectedLastName, actualLastName, "The actual last name is " + actualLastName
                + ", and it was expected to be " + expectedLastName);

        int actualTotalPrice = responseUpdate.jsonPath().getInt("totalprice");
        int expectedTotalPrice = 150;
        Assertions.assertEquals(expectedTotalPrice,actualTotalPrice, "The actual total price is " + actualTotalPrice
                + ", and it was expected to be " + expectedTotalPrice);

        boolean actualDepositPaid = responseUpdate.jsonPath().getBoolean("depositpaid");
        boolean expectedDepositPaid = false;
        Assertions.assertEquals(expectedDepositPaid,actualDepositPaid, "The actual deposit paid status is " + actualDepositPaid
                + ", and it was expected to be " + expectedDepositPaid);

        String actualCheckIn = responseUpdate.jsonPath().getString("bookingdates.checkin");
        String actualCheckOut = responseUpdate.jsonPath().getString("bookingdates.checkout");
        String expectedCheckIn = "2021-06-03";
        String expectedCheckOut = "2021-06-10";
        Assertions.assertEquals(expectedCheckIn,actualCheckIn, "checkin in response is not expected");
        Assertions.assertEquals(expectedCheckOut,actualCheckOut, "checkout in response is not expected");

        responseUpdate.prettyPrint();
    }
}
