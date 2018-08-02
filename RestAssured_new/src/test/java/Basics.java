import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Basics {
    Properties properties = new Properties();

    @BeforeTest
    public void beforeTest() {
        FileInputStream eV = null;
        try {
            eV = new FileInputStream("C:\\Users\\lkrawczyk\\Documents\\RestAssured\\RestAssured_new\\src\\env\\environmentalVariables.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            properties.load(eV);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String key = "AIzaSyDoyakYFQ_zlHRb6Wi3UBC0H3I27NAY6zk";

    @Test
    public void test_get() {
        RestAssured.baseURI = properties.getProperty("BASEURI");

        given().
                param("location", "-33.8670522,151.1957362").
                param("radius", "500").
                param("key", key).
                when().
                get("/maps/api/place/nearbysearch/json").
                then().
                assertThat().statusCode(200).
                and().contentType(ContentType.JSON).
                and().body("results[0].name", equalTo("Sydney")).
                and().header("Server", "scaffolding on HTTPServer2");
    }

    @Test
    public void test_postAndDelete() {
        RestAssured.baseURI = properties.getProperty("BASEURI");

        Response response = given().
                queryParam("key", key).
                body("(...)").
                when().
                post("/maps/api/place/add/json"). // It's not available anymore :(
                then().
                statusCode(200).
                and().contentType(ContentType.JSON).
                and().body("status", equalTo("OK")).
                extract().response();

        String responseString = response.asString();
        JsonPath responseJson = new JsonPath(responseString);
        String place_id = responseJson.get("place_id");

        given().
                queryParam("key", key).
                body("{" +
                        "\"place_id\": \"" + place_id + "\"" +
                        "}").
                when().
                post("/maps/api/place/delete/json").
                then().
                statusCode(200).
                and().body("status", equalTo("OK"));
    }
}
