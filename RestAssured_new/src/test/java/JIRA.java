import io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class JIRA {
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
    public void test_createIssue() {
        RestAssured.baseURI = properties.getProperty("JIRABASEURI");

        given().
                header("Content-Type", "application/json").
                header("Cookie", Reusable.getSessionCookie()).
                body("{\n" +
                        "    \"fields\": {\n" +
                        "       \"project\":\n" +
                        "       {\n" +
                        "          \"key\": \"PROJ\"\n" +
                        "       },\n" +
                        "       \"summary\": \"REST ye merry gentlemen.\",\n" +
                        "       \"description\": \"Creating of an issue using project keys and issue type names using the REST API\",\n" +
                        "       \"issuetype\": {\n" +
                        "          \"name\": \"Zadanie\"\n" +
                        "       }\n" +
                        "   }\n" +
                        "}").
                when().
                post("/rest/api/2/issue").
                then().
                statusCode(201);
    }
}
