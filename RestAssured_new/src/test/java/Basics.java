import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Basics {
    Properties properties = new Properties();

    @BeforeTest
    public void beforeTest() {
        FileInputStream eV = null;
        try {
            eV = new FileInputStream(System.getProperty("user.dir") + "\\src\\env\\environmentalVariables.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            properties.load(eV);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_get() {
        RestAssured.baseURI = properties.getProperty("BASEURI");

        Response response = given().
                param("location", "-33.8670522,151.1957362").
                param("radius", "500").
                param("key", properties.getProperty("KEY")).
                log().all().
                when().
                get("/maps/api/place/nearbysearch/json").
                then().
                assertThat().statusCode(200).
                and().contentType(ContentType.JSON).
                and().body("results[0].name", equalTo("Sydney")).
                and().header("Server", "scaffolding on HTTPServer2").
                log().all().
                extract().response();

        JsonPath responseJson = Reusable.rawToJson(response);
        int responseSize = responseJson.get("results.size()");
        for (int i = 0; i < responseSize; i++) {
            System.out.println(responseJson.get("results[" + i + "].name"));
        }
    }

    @Test
    public void test_postAndDelete_json() {
        RestAssured.baseURI = properties.getProperty("BASEURI");

        Response response = given().
                queryParam("key", properties.getProperty("KEY")).
                body("(...)").
                when().
                post("/maps/api/place/add/json"). // It's not available anymore :(
                then().
                statusCode(200).
                and().contentType(ContentType.JSON).
                and().body("status", equalTo("OK")).
                extract().response();

        JsonPath responseJson = Reusable.rawToJson(response);
        String place_id = responseJson.get("place_id");

        given().
                queryParam("key", properties.getProperty("KEY")).
                body("{" +
                        "\"place_id\": \"" + place_id + "\"" +
                        "}").
                when().
                post("/maps/api/place/delete/json").
                then().
                statusCode(200).
                and().body("status", equalTo("OK"));
    }

    private String generateStringFromXml(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    @Test
    public void test_postAndDelete_xml() throws IOException {
        RestAssured.baseURI = properties.getProperty("BASEURI");
        String xmlPath = System.getProperty("user.dir") + "\\src\\resources\\post_addPlace.xml";

        Response response = given().
                queryParam("key", properties.getProperty("KEY")).
                body(generateStringFromXml(xmlPath)).
                when().
                post("/maps/api/place/add/xml"). // It's not available anymore :(
                then().
                statusCode(200).
                and().contentType(ContentType.XML).
                extract().response();

        XmlPath responseXml = Reusable.rawToXml(response);
        String place_id = responseXml.get("PlaceAddResponse.place_id");

//        given().
//                queryParam("key", properties.getProperty("KEY")).
//                body("{" +
//                        "\"place_id\": \"" + place_id + "\"" +
//                        "}").
//                when().
//                post("/maps/api/place/delete/json").
//                then().
//                statusCode(200).
//                and().body("status", equalTo("OK"));
    }
}
