package practice;

import cucumber.api.java.cs.A;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;


import static org.junit.Assert.assertEquals;

public class GetBookingIDsWithFilterTest {

    @Test
    public void getBookingIDsWithFilterTest() {
        // get response with booking id's
        Response response =
                RestAssured.get("https://restful-booker.herokuapp.com/booking/6");
        response.print();

        // verify that the status code is 200
        int expected = 200;
        int actual = response.getStatusCode();
        response.prettyPrint();
        /*
        {
            "firstname": "Sally",
            "lastname": "Smith",
            "totalprice": 394,
            "depositpaid": true,
            "bookingdates": {
                "checkin": "2019-01-07",
                "checkout": "2020-05-27"
            }
        }
         */

        Assertions.assertEquals(expected, actual, "It's supposed to be 200, but it is " + actual);

        System.out.println("The actual status code is " + actual + ", and it's supposed to be " + expected + ".");

        // assert that there's at least one booking id in the list
        String actualFirstName = response.jsonPath().getString("firstname");
        String expectedFirstName = "Sally";
        Assertions.assertEquals(expectedFirstName, actualFirstName, "The name in the response is "
                + actualFirstName + ", and it's supposed to be " + expectedFirstName);
        System.out.println("The name in response is "
                + actualFirstName + ", and it's supposed to be " + expectedFirstName + ".");

        String actualLastName = response.jsonPath().getString("lastname");
        String expectedLastName = "Jones";
        Assertions.assertEquals(expectedLastName, actualLastName, "The actual last name is " + actualLastName
        + ", and it was expected to be " + expectedLastName);

        int actualTotalPrice = response.jsonPath().getInt("totalprice");
        int expectedTotalPrice = 908;
        Assertions.assertEquals(expectedTotalPrice,actualTotalPrice, "The actual total price is " + actualTotalPrice
                + ", and it was expected to be " + expectedTotalPrice);

        boolean actualDepositPaid = response.jsonPath().getBoolean("depositpaid");
        boolean expectedDepositPaid = false;
        Assertions.assertEquals(expectedDepositPaid,actualDepositPaid, "The actual deposit paid status is " + actualDepositPaid
                + ", and it was expected to be " + expectedDepositPaid);

        String actualCheckIn = response.jsonPath().getString("bookingdates.checkin");
        String actualCheckOut = response.jsonPath().getString("bookingdates.checkout");
        String expectedCheckIn = "2016-06-09";
        String expectedCheckOut = "2021-01-18";
        Assertions.assertEquals(expectedCheckIn,actualCheckIn, "checkin in response is not expected");
        Assertions.assertEquals(expectedCheckOut,actualCheckOut, "checkout in response is not expected");
    }
}
