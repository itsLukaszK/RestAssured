import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

import java.util.Properties;

import static io.restassured.RestAssured.given;


public class Reusable {
    static JsonPath rawToJson(Response response) {
        String responseString = response.asString();
        return new JsonPath(responseString);
    }

    static XmlPath rawToXml(Response response) {
        String responseString = response.asString();
        return new XmlPath(responseString);
    }

    static String getSessionCookie() {
        Properties properties = new Properties();
        RestAssured.baseURI = properties.getProperty("JIRABASEURI");

        Response response = given().
                header("Content-Type", "application/json").
                body("{ \"username\": \"admin\", \"password\": \"admin\" }").
                when().
                post("/rest/auth/1/session").
                then().
                extract().response();

        JsonPath responseJson = rawToJson(response);
        String sessionName = responseJson.get("session.name");
        String sessionValue = responseJson.get("session.value");
        return sessionName + "=" + sessionValue;
    }
}
