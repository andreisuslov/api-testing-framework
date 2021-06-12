package practice;

import cucumber.api.java.cs.A;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import utilities.HelperMethods;


import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GetBookingIDsTest extends HelperMethods {

    @Test
    public void getBookingIDsInJsonFormatTest() {

        // Create booking
        Response responseCreate = createBooking();
        responseCreate.prettyPrint();

    /*
        {
            "bookingid": 16,
                "booking": {
            "firstname": "Andrei",
                    "lastname": "Suslov",
                    "totalprice": 150,
                    "depositpaid": false,
                    "bookingdates": {
                "checkin": "2021-06-03",
                        "checkout": "2021-06-10"
            },
            "additionalneeds": "Swimming Pool"
        }
        }
    */

        // Set path parameter
        rs.pathParam("bookingId", responseCreate.jsonPath().getInt("bookingid"));


        // get response with booking id's
        Response response =
                RestAssured.given(rs).get("/booking/{bookingId}");
        response.prettyPrint(); // prints out the response body

        // Get response with booking
        Header json = new Header("Accept", "application/json");
        rs.header(json);

        // verify that the status code is 200
        int expected = 200;
        int actual = response.getStatusCode();

        Assertions.assertEquals(expected, actual, "It's supposed to be 200, but it is " + actual);

        System.out.println("The actual status code is " + actual + ", and it's supposed to be " + expected + ".");

        // assert that there's at least one booking id in the list
        String actualFirstName = response.jsonPath().getString("firstname");
        String expectedFirstName = "Andrei";
        Assertions.assertEquals(expectedFirstName, actualFirstName, "The name in the response is "
                + actualFirstName + ", and it's supposed to be " + expectedFirstName);
        System.out.println("The actual name in response is "
                + actualFirstName + ", and it's supposed to be " + expectedFirstName + ".");

        String actualLastName = response.jsonPath().getString("lastname");
        String expectedLastName = "Suslov";
        Assertions.assertEquals(expectedLastName, actualLastName, "The actual last name is " + actualLastName
                + ", and it was expected to be " + expectedLastName);
        System.out.println("The actual surname in response is "
                + actualLastName + ", and it's supposed to be " + expectedLastName + ".");

        int actualTotalPrice = response.jsonPath().getInt("totalprice");
        int expectedTotalPrice = 150;
        Assertions.assertEquals(expectedTotalPrice, actualTotalPrice, "The actual total price is " + actualTotalPrice
                + ", and it was expected to be " + expectedTotalPrice);
        System.out.println("The actual total price in response is "
                + actualTotalPrice + ", and it's supposed to be " + expectedTotalPrice + ".");

        boolean actualDepositPaid = response.jsonPath().getBoolean("depositpaid");
        boolean expectedDepositPaid = false;
        Assertions.assertEquals(expectedDepositPaid, actualDepositPaid, "The actual deposit paid status is " + actualDepositPaid
                + ", and it was expected to be " + expectedDepositPaid);
        System.out.println("The actual \"expected deposit paid in\" response is "
                + actualDepositPaid + ", and it's supposed to be " + expectedDepositPaid + ".");

        String actualCheckIn = response.jsonPath().getString("bookingdates.checkin");
        String actualCheckOut = response.jsonPath().getString("bookingdates.checkout");
        String expectedCheckIn = "2021-06-03";
        String expectedCheckOut = "2021-06-10";
        Assertions.assertEquals(expectedCheckIn, actualCheckIn, "checkin in response is not expected");
        Assertions.assertEquals(expectedCheckOut, actualCheckOut, "checkout in response is not expected");
        System.out.println("The actual check-in date in response is "
                + actualCheckIn + ", and it's supposed to be " + expectedCheckIn + ".");
        System.out.println("The actual check-out date in response is "
                + actualCheckOut + ", and it's supposed to be " + expectedCheckOut + ".");
    }

    @Test
    public void getBookingIDsInXmlFormatTest() {

        // Create booking
        Response responseCreate = createBooking();
        responseCreate.prettyPrint();

    /*
        {
            "bookingid": 16,
                "booking": {
            "firstname": "Andrei",
                    "lastname": "Suslov",
                    "totalprice": 150,
                    "depositpaid": false,
                    "bookingdates": {
                "checkin": "2021-06-03",
                        "checkout": "2021-06-10"
            },
            "additionalneeds": "Swimming Pool"
        }
        }
    */

        // Set path parameter
        rs.pathParam("bookingId", responseCreate.jsonPath().getInt("bookingid"));

        // Get response with booking
        Header xml = new Header("Accept", "application/xml");
        rs.header(xml);
        Response response = RestAssured.given(rs).get("/booking/{bookingId}");
        response.prettyPrint();


        // verify that the status code is 200
        int expected = 200;
        int actual = responseCreate.getStatusCode();


        Assertions.assertEquals(expected, actual, "It's supposed to be 200, but it is " + actual);
//
        System.out.println("The actual status code is " + actual + ", and it's supposed to be " + expected + ".");
//
        String actualFirstName = response.xmlPath().getString("booking.firstname");
        String expectedFirstName = "Andrei";
        Assertions.assertEquals(expectedFirstName, actualFirstName, "The name in the response is "
                + actualFirstName + ", and it's supposed to be " + expectedFirstName);
        System.out.println("The name in response is "
                + actualFirstName + ", and it's supposed to be " + expectedFirstName + ".");

        String actualLastName = response.xmlPath().getString("booking.lastname");
        String expectedLastName = "Suslov";
        Assertions.assertEquals(expectedLastName, actualLastName, "The actual last name is " + actualLastName
                + ", and it was expected to be " + expectedLastName);

        int actualTotalPrice = response.xmlPath().getInt("booking.totalprice");
        int expectedTotalPrice = 150;
        Assertions.assertEquals(expectedTotalPrice, actualTotalPrice, "The actual total price is " + actualTotalPrice
                + ", and it was expected to be " + expectedTotalPrice);

        boolean actualDepositPaid = response.xmlPath().getBoolean("booking.depositpaid");
        boolean expectedDepositPaid = false;
        Assertions.assertEquals(expectedDepositPaid, actualDepositPaid, "The actual deposit paid status is " + actualDepositPaid
                + ", and it was expected to be " + expectedDepositPaid);

        String actualCheckIn = response.xmlPath().getString("booking.bookingdates.checkin");
        String actualCheckOut = response.xmlPath().getString("booking.bookingdates.checkout");
        String expectedCheckIn = "2021-06-03";
        String expectedCheckOut = "2021-06-10";
        Assertions.assertEquals(expectedCheckIn, actualCheckIn, "checkin in response is not expected");
        Assertions.assertEquals(expectedCheckOut, actualCheckOut, "checkout in response is not expected");
    }
}
