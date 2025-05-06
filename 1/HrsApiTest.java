public class HrsApiTest extends ApiTestBase {

    @Test
    void calculateCallCost_ShouldReturnValidAmount() {
        given()
            .contentType(ContentType.JSON)
            .body(TestData.callForCalculation())
        .when()
            .post("/api/calculate")
        .then()
            .statusCode(200)
            .body("amount", greaterThan(0f));
    }

    @Test
    void getTariffInfo_ShouldReturnValidData() {
        when()
            .get("/api/tariffs/11")
        .then()
            .statusCode(200)
            .body("name", equalTo("Классика"));
    }
}