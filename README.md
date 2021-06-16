[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com) [![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)


# API-testing-restful-booker-rest-assured

# Rest Assured Automation with Restful Booker App

## What is Rest Assured?
Rest Assured is one of the many Java HTTP clients. 
It provides us with **BDD syntax:** **given** (headers), 
**when** (endpoint, HTTP method type,  body), 
**then** (validate the response code and body). 
Using this library, we can perform automation of CRUD:

`get`, 

`post`, 

`put`, 

`patch`, and 

`delete` request methods.

##Path vs Query parameters

**Path parameters** are used to identify a specific entity.
```
/booking/{bookingID}
```
**Query parameters** are used to filter that specific resource.
```
/booking/{bookingID}?firstname=Andrei
```

## Headers
How to get headers?
```
Headers headers = response.getHeaders();
        System.out.println("Headers: " + headers);
```
How to get a single header?

We can get a single header's value using 2 ways:

1. ``` Header serverHeader1 = headers.get("Server");
        System.out.println(serverHeader1.getName() + ":" + serverHeader1.getValue()); ```
	
2.   ``` String serverHeader2 = response.getHeader("Server");
        System.out.println("Server: " + serverHeader2); ```
	
We can also add a header:

```
Header someHeader = new Header("some_name", "some_value");
        rs.header(someHeader);
```
	
## Cookies
How to get cookies?
 ```
 Cookies cookies = response.getDetailedCookies();
        System.out.println("Cookies: " + cookies); // prints no cookies in the first place
```

We can also add a cookie:

```
Cookie someCookie = new Cookie.Builder("some_name", "some_value").build();
        rs.cookie(someCookie);
```

If you want to make sure that you've added all cookies and headers, use `.log().all()` syntax:

```
Response response = RestAssured.given(rs).
                log().all().
                get("/ping");
```

## (De)serialization
### Serialization
POJO – Plain Old Java Objects.
We will use Jackson Databind dependency for serialization purposes:
```
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.10.1</version>
</dependency>
```
Also, you can use GSON dependency as an alternative.

We'll need to create a POJO classes to represent JSON body,
so for this object (which I copied from the documentation)
```
curl -X POST \
  https://restful-booker.herokuapp.com/booking \
  -H 'Content-Type: application/json' \
  -d '{
    "firstname" : "Jim",
    "lastname" : "Brown",
    "totalprice" : 111,
    "depositpaid" : true,
    "bookingdates" : {
        "checkin" : "2018-01-01",
        "checkout" : "2019-01-01"
    },
    "additionalneeds" : "Breakfast"
}'
```
You need to create two separate classes, 
as we have one JSON object - `bookingdates` - embedded into another one.
```
public class Booking {
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private BookingDates bookingdates; // a separate JSON object
    private String additionalneeds;
}
```

When creating such a class, you need to make sure that you have an exact
match of the instance variables' names and of the field of JSON-body.

As you can see, `"firstname" : "Jim"` becomes `private String firstname;`, omitting Java
naming conventions.

Then, in a `CreateBookingTests` class, create another method 
`createBookingWithPojoTest()` to practice serialization. Inside,
we can create a JSON-body using POJO's.
First, we need to create an instance of `BookingDates` class, 
and we can assign there the values to the dates. Then, we did the same
for the instance of `Booking` class.

```
BookingDates bookingdates = new BookingDates("2021-06-03", "2021-06-10");
Booking booking = new Booking(
        "Andrei",
        "Suslov",
        150,
        false,
        bookingdates,
        "Swimming Pool");
```

### Deserialization



## CRUD Operations
### **Automating GET (Read)**
In my API testing approach, I perform the following steps:

1. Get the response.

``` 
Response response = 
	RestAssured.get("https://restful-booker.herokuapp.com/booking/6");
response.print(); // prints out the response body 
```

Using the more advanced approach, we may introduce variable
`rs` which stands for request specification.
We create Request Specification variable using Request Spec Builder
to set up Base URI in order to avoid redundancy and improve re-usability.
``` 
RequestSpecification rs =
    new RequestSpecBuilder().
        setBaseUri("https://restful-booker.herokuapp.com").build();
``` 
So this way,
every time we are using BDD syntax, we can place that `rs` variable into the `.given()` pre-condition part:
``` 
given().
    spec(rs).
when().
    get("/your-endpoint").
then().
    assertThat().statusCode(200);
``` 
2. Verify the status code – we verify whether an actual response is equal to expected.

```
    int expected = 200;
    int actual = response.getStatusCode();
    response.prettyPrint();
```

3. Do validations and assertions. For that purpose, I use Junit Assertions. And because we are using JSON format, use `.jsonPath()` method to get values from the response. Inside that method there are different other methods to get different values, such as: `.getString()`, `.getList()`, `.getBoolean()`, `.getInt()`. 

```
String actualFirstName = response.jsonPath().getString("firstname");
String expectedFirstName = "Jim";

assertEquals(expectedFirstName, actualFirstName, 
"The name in the response is " + actualFirstName 
+ ", and it's supposed to be " + expectedFirstName);
```

### **Automating POST (Create)**
We need to have a JSON body to submit it to a certain endpoint:

1. Create JSON body:

Look into the documentation and copy the JSON body from there.

```
{
"firstname": "Sally",
"lastname": "Brown",
"totalprice": 111,
"depositpaid": true,
"bookingdates": {
"checkin": "2013-02-23",
"checkout": "2014-10-23"
},
"additionalneeds": "Breakfast"
}
```
Then, create an object alike.

```
JSONObject body = new JSONObject();
body.put("firstname", "Andrei");
body.put("lastname", "Suslov");
body.put("totalprice", 150);
body.put("depositpaid", false);
```
If we have a sub-object that contains some info, we need to create another JSON object and put it into our main JSON body.

1. Create JSON body:
```
JSONObject bookingDates = new JSONObject();
bookingDates.put("checkin", "2021-03-13");
bookingDates.put("checkout", "2021-03-19");
body.put("bookingDates", bookingDates);
body.put("additionalneeds", "Cold Air Conditioner");
```

2. Get a response.

With the POST method, we need to submit this body object and let RestAssured know that this is a JSON body using the `contentType(ContentType.JSON)` method.

```
Response response =
RestAssured.given().contentType(ContentType.JSON).body(body.toString())
.post("https://restful-booker.herokuapp.com/booking";**
```

3. Assert all fields as you did using the `GET` method.

```
String actualFirstName = response.jsonPath().getString("firstname");
String expectedFirstName = "Andrei";
assertEquals(expectedFirstName, actualFirstName, 
"The name in the response is " + actualFirstName 
+ ", and it's supposed to be " + expectedFirstName);
```

### **Automating PUT (Update)**
`PUT` method is considered _idempotent_. 

**Why is so?** If you issue the same request repeatedly, 
it should always have the same outcome 
(the data you sent is now replaces the entire data of the entity).

`PUT` method is used to update the existing resource. 
It does not update just some fields in existing resources, 
it updates the full resource, so basically `PUT` means **replace**. 
It is similar to the `POST` method as it requires the request 
body with all fields (parameters) because you're replacing **the entire entity**.
When using PUT, it is assumed that you are sending the complete entity, 
and that complete entity replaces any existing entity at that URI.

1. Create JSON body:
```
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
```
  

2.  Get the response

```
Response responseCreate = RestAssured.given()
.contentType(ContentType.JSON).body(body.toString())
.post("https://restful-booker.herokuapp.com/booking)");
```

3. Create the new object to update it with the one we just created.

```
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
```

4. Get the ID of the object we created in the first place to update it with a newly created one.

```
int bookingId = responseCreate.jsonPath().getInt("bookingid");
```

5. Get the response using ID.

```
Response responseUpdate = RestAssured.given()
.auth().preemptive().basic("admin", "password123")
.contentType(ContentType.JSON).body(body.toString())
.put("https://restful-booker.herokuapp.com/booking/" + bookingId);
```

6. Do validations and assertions.

```
String actualFirstName = responseUpdate.jsonPath().getString("firstname");
String expectedFirstName = "Irina";
Assertions.assertEquals(expectedFirstName, actualFirstName, 
"The name in the response is "+ actualFirstName + 
", and it's supposed to be " + expectedFirstName);

String actualLastName =
responseUpdate.jsonPath().getString("lastname");
String expectedLastName = "Suslov";
Assertions.assertEquals(expectedLastName, actualLastName, 
"The actual last name is " + actualLastName
+ ", and it was expected to be " + expectedLastName);
…
```


### **Automating PATCH (Update Partially)**
`PATCH` isn't idempotent.

**Why is so?** Suppose you patch not a specific endpoint's entity 
(ex.: `/users/{id}`) but the whole resource (ex.: `/users`).
In this case, once we issue the exact same PATCH request _twice_,
we will get a duplicate entity in our endpoint.

**BUT**: All this comes down to how a server is designed to handle requests.
If the server allows patching the whole endpoint/resource rather than a particular
entity (ex.: `/users/{id}`) and if that endpoint/resource (ex.: `/users`) allows duplicate usernames,
then `PATCH` request is not idempotent.



The `PATCH` method is used to **update** an object partially, 
it "updates a current booking with a partial payload" as it's stated in
herokuapp documentation. 

### Automation steps of issuing a `PATCH` request
1. Create JSON body.
2. Update the data partially.
```
JSONObject body = new JSONObject();
body.put("firstname", "Irina");

JSONObject bookingdates = new JSONObject();
bookingdates.put("checkin", "2021-06-01");
body.put("bookingdates", bookingdates);
```

3. Get the ID of the object we created in the first place to update it with a newly created one.

```
int bookingId = responseCreate.jsonPath().getInt("bookingid");
```

4. Get the response using ID.
```
Response responseUpdate = RestAssured.given()  
  .auth().preemptive().basic("admin", "password123")  
  .contentType(ContentType.JSON).body(body.toString())  
  .patch("https://restful-booker.herokuapp.com/booking/" + bookingId);
```

5. Do validations and assertions.

```
String actualFirstName = 
responseUpdate.jsonPath().getString("firstname");
String expectedFirstName = "Irina";
Assertions.assertEquals(expectedFirstName, actualFirstName, 
"The name in the response is "+ actualFirstName + 
", and it's supposed to be " + expectedFirstName);
  
String expectedCheckIn = "2021-06-01";
Assertions.assertEquals(expectedCheckIn, actualCheckIn, 
"Checkin-in date in response is not expected");
```

### **Automating DELETE**
1. Create JSON body.
2. Get ID of the created JSON object.
```
int bookingid = responseCreate.jsonPath().getInt("bookingid");
```
3. Delete object with an ID. We don’t need Content Type and Content Body.

```
Response responseDelete = 
RestAssured.given().auth().preemptive().basic("admin", "password123")
.delete("https://restful-booker.herokuapp.com/booking/" + bookingid);

responseDelete.print();
```
4. Verify status code 201.

```
Assertions.assertEquals(201, responseDelete.getStatusCode(), 
"Status code should be 201, but it's not.");
Response responseGet = 
RestAssured.get("https://restful-booker.herokuapp.com/booking/" 
+ bookingid);

responseGet.print(); // prints Created
```

5. If we try to get the deleted body, we’ll receive a message “Not Found” which doesn’t have curly braces, so the body alway consists of text and to get it we need to use the following syntax:
```
Assertions.assertEquals("Not Found", responseGet.getBody().asString(), 
"Body should be 'Not Found', but it's not.");
```

