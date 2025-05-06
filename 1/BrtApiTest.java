public class BrtApiTest extends ApiTestBase {

    @Test
    void processCall_ShouldSaveCallData() {
        given()
            .contentType(ContentType.JSON)
            .body(TestData.validCallRecord())
        .when()
            .post("/api/calls")
        .then()
            .statusCode(201);
    }

    @Test
    void getSubscriberInfo_ShouldReturnValidData() {
        given()
            .pathParam("msisdn", "89991112233")
        .when()
            .get("/api/subscribers/{msisdn}")
        .then()
            .statusCode(200)
            .body("balance", is(notNullValue()));
    }
}