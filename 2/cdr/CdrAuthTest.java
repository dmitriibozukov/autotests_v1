import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CdrAuthTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080"; // URL BRT сервиса
    }

    @Test
    void whenSendValidCdrFile_ShouldReturn200() {
        File cdrFile = new File("src/test/resources/test-cdr-valid.csv");

        given()
            .auth().basic("cdr_user", "password") // Учетные данные для авторизации
            .contentType(ContentType.TEXT)
            .body(cdrFile)
        .when()
            .post("/api/cdr/upload")
        .then()
            .statusCode(200)
            .body("processedRecords", equalTo(2))
            .body("failedRecords", equalTo(0));
    }

    @Test
    void whenSendInvalidCdrFile_ShouldReturn400() {
        File cdrFile = new File("src/test/resources/test-cdr-invalid.csv");

        given()
            .auth().basic("cdr_user", "password")
            .contentType(ContentType.TEXT)
            .body(cdrFile)
        .when()
            .post("/api/cdr/upload")
        .then()
            .statusCode(400)
            .body("error", containsString("Invalid CDR format"));
    }
}