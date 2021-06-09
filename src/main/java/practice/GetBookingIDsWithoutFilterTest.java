package practice;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;


import static org.junit.Test.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GetBookingIDsWithoutFilterTest {

    @Test
    public void getBookingIDsWithoutFilterTest() {
        // get response with booking id's
        Response response =
                RestAssured.get("https://restful-booker.herokuapp.com/booking");
        response.print(); // prints out the response body

        // verify that the status code is 200
        int expected = 200;
        int actual = response.getStatusCode();
        assertEquals(actual, expected, "It's supposed to be 200, but it is " + actual);
        System.out.println("The actual status code is " + 200 + ", and it's supposed to be " + expected + ".");

        // assert that there's at least one booking id in the list
        List<Integer> bookingIDsList = response.jsonPath().getList("bookingid");
        assertFalse(bookingIDsList.isEmpty(), "The booking ID list is not empty");


    }
}
