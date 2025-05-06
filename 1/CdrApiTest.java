public class CdrApiTest extends ApiTestBase {

    @Test
    void generateCdrFile_ShouldReturnAccepted() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .post("/api/cdr/generate")
        .then()
            .statusCode(202);
    }

    @Test
    void getLastCdrFile_ShouldReturnValidStructure() {
        when()
            .get("/api/cdr/last")
        .then()
            .statusCode(200)
            .body("records.size()", greaterThanOrEqualTo(0));
    }
}