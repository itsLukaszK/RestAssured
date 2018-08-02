import io.restassured.RestAssured;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class Basics {
    @Test
    public void test_basic() {
        RestAssured.baseURI = "https://maps.googleapis.com";

        given().
                param("location", "-33.8670522,151.1957362").
                param("radius", "500").
                param("key", "AIzaSyDoyakYFQ_zlHRb6Wi3UBC0H3I27NAY6zk").
                when().
                get("/maps/api/place/nearbysearch/json").
                then().
                assertThat().statusCode(200);
    }
}
