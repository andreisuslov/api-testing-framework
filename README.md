
# API-testing-restful-booker-rest-assured

# Rest Assured Automation with Restful Booker App

Rest Assured is one of the many Java HTTP clients. It provides us with **BDD syntax:** **given** (headers), **when** (endpoint, HTTP method type,  body), **then** (validate the response code and body). Using this library, we can perform automation of `get`, `post`, `put`, `patch`, and `delete` request methods.

## **Automating GET**
In my API testing approach, I perform the following steps:

1. Get the response.

``` 
Response response = 
	RestAssured.get("https://restful-booker.herokuapp.com/booking/6");
response.print(); // prints out the response body 
```

1. Verify the status code – we verify whether an actual response is equal to expected.

```
    int expected = 200;
    int actual = response.getStatusCode();
    response.prettyPrint();
```

1. Do validations and assertions. For that purpose, I use Junit Assertions. And because we are using JSON format, use `.jsonPath()` method to get values from the response. Inside that method there are different other methods to get different values, such as: `.getString*()`, `.getList()`, `.getBoolean()`, `.getInt()`. 

```
String actualFirstName = response.jsonPath().getString("firstname");
String expectedFirstName = "Jim";

*assertEquals(expectedFirstName, actualFirstName, 
"The name in the response is " + actualFirstName 
+ ", and it's supposed to be " + expectedFirstName);
```

## **Automating POST**
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

## **Automating PUT**
`PUT` method is used to update the existing resource. It does not update just some fields in existing resources, it updates the full resource, so basically `PUT` means **replace**. It is similar to the `POST` method as it requires the request body with all fields. The difference is the ID in the URL and the need for authentication.

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


## **Automating PATCH**
The `PATCH` method is used to update an object partially.

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

1. Do validations and assertions.

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

## **Automating DELETE**
1. Create JSON body.
1. Get ID of the created JSON object.
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
