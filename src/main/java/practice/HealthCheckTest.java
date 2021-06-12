package practice;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import utilities.HelperMethods;

import static io.restassured.RestAssured.*;

public class HealthCheckTest extends HelperMethods {

    @Test
    public void healthCheckTest(){

        given().
                spec(rs).
        when().
                get("/ping").
        then().
                // never trust a test that never fails
                // att first, you can put some random value
                // such as "249" and check the result
            assertThat().statusCode(201);
        System.out.println("Response: " +
                given().
                    spec(rs).
                when().
                     get("/ping").asString());
    }

    @Test
    public void getHeadersAndCookiesTest(){

        // Add a header
        Header someHeader = new Header("some_name", "some_value");
        rs.header(someHeader);

        // Add cookie
        Cookie someCookie = new Cookie.Builder("some_name", "some_value").build();
        rs.cookie(someCookie);

        // if you want to make sure that you've added all cookies and headers,
        // add .log().all() after .cookie().header() and before .get() methods.
        Response response = RestAssured.given(rs).
//                cookie("some_cookie_name", "some_cookie_value").
//                header("some_header_name", "some_header_value").
                log().all().
                get("/ping");


        // get headers
        Headers headers = response.getHeaders();
        System.out.println("Headers: " + headers);

        // we can get a single header's value using 2 ways:
        // 1.
        Header serverHeader1 = headers.get("Server");
        System.out.println(serverHeader1.getName() + ":" + serverHeader1.getValue());
        // 2.
        String serverHeader2 = response.getHeader("Server");
        System.out.println("Server: " + serverHeader2);

        // get cookies
        Cookies cookies = response.getDetailedCookies();
        System.out.println("Cookies: " + cookies); // prints no cookies in the first place


    }
}
