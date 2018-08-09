import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;


public class Reusable {
    static JsonPath rawToJson(Response response) {
        String responseString = response.asString();
        return new JsonPath(responseString);
    }

    static XmlPath rawToXml(Response response) {
        String responseString = response.asString();
        return new XmlPath(responseString);
    }
}
