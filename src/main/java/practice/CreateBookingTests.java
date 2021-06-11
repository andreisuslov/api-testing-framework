package practice;

import io.restassured.response.Response;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import utilities.HelperMethods;

public class CreateBookingTests extends HelperMethods{

    @Test
    public void createBookingTest() {
        // create booking
        Response response = createBooking();
        response.prettyPrint();

        // Verifications
        // Verify response 200
        int expected = 200;
        int actual = response.getStatusCode();
        response.prettyPrint();
        /*
                {
            "bookingid": 37,
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

        Assertions.assertEquals(expected, actual, "It's supposed to be 200, but it is " + actual);

        System.out.println("The actual status code is " + actual + ", and it's supposed to be " + expected + ".");

        // Verify All fields
        String actualFirstName = response.jsonPath().getString("booking.firstname");
        String expectedFirstName = "Andrei";
        Assertions.assertEquals(expectedFirstName, actualFirstName, "The name in the response is "
                + actualFirstName + ", and it's supposed to be " + expectedFirstName);
        System.out.println("The name in response is "
                + actualFirstName + ", and it's supposed to be " + expectedFirstName + ".");

        String actualLastName = response.jsonPath().getString("booking.lastname");
        String expectedLastName = "Suslov";
        Assertions.assertEquals(expectedLastName, actualLastName, "The actual last name is " + actualLastName
                + ", and it was expected to be " + expectedLastName);

        int actualTotalPrice = response.jsonPath().getInt("booking.totalprice");
        int expectedTotalPrice = 150;
        Assertions.assertEquals(expectedTotalPrice,actualTotalPrice, "The actual total price is " + actualTotalPrice
                + ", and it was expected to be " + expectedTotalPrice);

        boolean actualDepositPaid = response.jsonPath().getBoolean("booking.depositpaid");
        boolean expectedDepositPaid = false;
        Assertions.assertEquals(expectedDepositPaid,actualDepositPaid, "The actual deposit paid status is " + actualDepositPaid
                + ", and it was expected to be " + expectedDepositPaid);

        String actualCheckIn = response.jsonPath().getString("booking.bookingdates.checkin");
        String actualCheckOut = response.jsonPath().getString("booking.bookingdates.checkout");
        String expectedCheckIn = "2021-06-03";
        String expectedCheckOut = "2021-06-10";
        Assertions.assertEquals(expectedCheckIn,actualCheckIn, "checkin in response is not expected");
        Assertions.assertEquals(expectedCheckOut,actualCheckOut, "checkout in response is not expected");

    }

    }

